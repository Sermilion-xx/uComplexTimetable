package Calculator;
import Database.*;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Range;

import java.util.*;
import java.util.stream.Collectors;

public final class Utils {

    public static Lesson makeLesson(Load load, int type, int hours, int roomType, int subgroup){
        return new Lesson(load.getCourse(), load.getTeacher(), hours, roomType, type, subgroup);
    }

    public static ArrayList<Load> checkOtherGroupsForCombination(List<Load> loads, Load load, int hours, int hoursToAllocate, int largestCapacity, Allocation allocation){
        ArrayList<Load> result = new ArrayList<>();
        int size = load.getGroup().getSize();
        for (Load _load: loads){
            if(_load.getGroup().getId() != load.getGroup().getId() && _load.getCourse() == load.getCourse() && load.getLecture()>0){
                int _hours = Math.round((float) _load.getLecture() / 17);
                if (_hours == 0) _hours++;

                String index = _load.getGroup().getId() + ":" + _load.getCourse() + ":" + _load.getTeacher() + ":" + 0 + ":-1";
                int _hoursToAllocate = allocation.getHoursToAllocate(index, _hours);

                if (_hoursToAllocate == hoursToAllocate && _hours == hours && (size+_load.getGroup().getSize()) < largestCapacity) {
                    size = size + _load.getGroup().getSize();
                    result.add(_load);
                }
            }
        }
        return result;
    }

    public static List<Lesson> makeLessons(List<Load> loads, int largestCapacity, Allocation allocation) {
        List<Lesson> lessons = new ArrayList<>();
        List<Load> excluded = new ArrayList<>();
        int l=0;
        int s=0;
        int la=0;

        int hours;
        String index;
        int hoursToAllocate;
        for (Load load : loads) {
            if (load.getLecture() > 0) {
                if (!excluded.contains(load)) {
                    hours = Math.round((float) load.getLecture() / 17);
                    if (hours == 0) hours++;
                    index = load.getGroup().getId() + ":" + load.getCourse() + ":" + load.getTeacher() + ":" + 0 + ":-1";
                    hoursToAllocate = allocation.getHoursToAllocate(index, hours);
                    if(hoursToAllocate > 0) {
                        l++;
                        Lesson lesson = makeLesson(load, 0, hoursToAllocate, load.getLectureRoom(), -1);
                        ArrayList<Load> checks = checkOtherGroupsForCombination(loads, load, hours, hoursToAllocate, largestCapacity, allocation);
                        for (Load check : checks) {
                            excluded.add(check);
                            lesson.addGroup(check.getGroup());
                        }
                        lesson.addGroup(load.getGroup());
                        lessons.add(lesson);
                    }
                }
            }
            if (load.getSeminar() > 0) {
                hours = Math.round((float) load.getSeminar() / 17);
                if (hours == 0) hours++;
                for (int i=1; i<=load.getSubgroup(); i++) {
                    index = load.getGroup().getId() + ":" + load.getCourse() + ":" + load.getTeacher() + ":" + 1 + ":" + i;
                    hoursToAllocate = allocation.getHoursToAllocate(index, hours);
                    if(hoursToAllocate > 0){
                        Lesson lesson = makeLesson(load, 1, hoursToAllocate, load.getSeminarRoom(), i);
                        lesson.addGroup(load.getGroup());
                        lessons.add(lesson);
                        s++;
                    }
                }
            }
            if (load.getLab() > 0) {
                hours = Math.round((float) load.getLab() / 17);
                if (hours == 0) hours++;
                for (int i=1; i<=load.getSubgroup(); i++) {
                    index = load.getGroup().getId() + ":" + load.getCourse() + ":" + load.getTeacher() + ":" + 2 + ":" + i;
                    hoursToAllocate = allocation.getHoursToAllocate(index, hours);
                    if(hoursToAllocate > 0) {
                        Lesson lesson = makeLesson(load, 2, hoursToAllocate, load.getLabRoom(), i);
                        lesson.addGroup(load.getGroup());
                        lessons.add(lesson);
                        la++;
                    }
                }
            }
        }
        Collections.sort(lessons);
        System.out.println("Создано " + lessons.size() + " занятий.");
        System.out.println(l + " " + s + " " + la);
        return lessons;
    }

    public static int getGroupHoursAverage(List<Lesson> lessons, Lesson lesson){
        int hours = 0;
        Group group = lesson.getGroups().get(0);
        for(Lesson _lesson:lessons) if (_lesson.getGroups().contains(group)) hours = hours + _lesson.getHours();
        return (int) Math.ceil(hours/2) + 1;
    }

    public static List<Room> sortRooms(List<Room> rooms) {
        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room r1, Room r2) {
                return r1.getSeats() - r2.getSeats();
            }
        });
        return rooms;
    }

    public static Allocation makeAllocation(List<Object[]> entries, List<Group> groups) {
        Allocation allocation = new Allocation();
        for (Object[] object : entries) {
            Entry entry = (Entry) object[0];
            Content content = (Content) object[1];
            int teacher = entry.getTeacher();
            Room room = entry.getRoom();
            Group group = content.getGroup();
            int week = content.getWeek();
            int day = content.getDay();
            int hour = content.getHour();
            int slot = 42 * (week - 1) + 6 * (day - 1) + hour;
            allocation.putTeacherAllocation(teacher, slot, content.getGroup().getFaculty().getBuilding());
            allocation.putRoomAllocation(room.getId(), slot);
            allocation.putGroupAllocation(group.getId(), slot, entry.getP());
            if (groups.contains(content.getGroup())){
                String index = entry.getGroup() + ":" + entry.getCourse() + ":" + entry.getTeacher() + ":" + content.getType() + ":" + entry.getP();
                allocation.putAllocatedLoad(index);
            }
        }
        return allocation;
    }
    public static List<Integer> getTableSlots(String hours) {
        String[] hoursList = hours.split(",");
        ArrayList<Integer> slots = new ArrayList<>();
        ImmutableSortedSet<Integer> hoursSet = ContiguousSet.create(Range.closed(Integer.valueOf(hoursList[0]), Integer.valueOf(hoursList[1])), DiscreteDomain.integers());
        for (int i = 0; i <= 13; i++) {
            for (int hour : hoursSet) {
                int s = hour + 6 * i;
                slots.add(s);
            }
        }
        return slots;
    }
    public static int[] slotToNativeTime(int slot){
        int[] time = new int[3];
        time[0] = 1;
        if (slot > 42) {
            time[0] = 2;
            slot = slot - 42;
        }
        time[2] = slot % 6;
        if (time[2] == 0) time[2] = 6;
        time[1] = (slot - time[2]) / 6 + 1;
        return time;
    }


    public static List<Room> getRoomsByFaculty(List<Room> rooms, Faculty faculty) {
        return rooms.stream().filter(room -> room.getFaculty().equals(faculty)).collect(Collectors.toList());
    }
    public static List<Room> getRoomsByBuilding(List<Room> rooms, Faculty faculty) {
        return rooms.stream().filter(room -> room.getFaculty().getBuilding() == faculty.getBuilding()).collect(Collectors.toList());
    }

    public static Room pickRoom(List<Room> rooms, Lesson lesson, Allocation allocation, int slot) {
        int type = lesson.getRoomType();
        int size = 0;
        for (Group group: lesson.getGroups()){
            size = size + group.getSize();
        }
        for (Room room : rooms) {
            if (room.getSeats() >= size && room.getType() == type && allocation.isRoomAvailable(room.getId(), slot)) {
                return room;
            }
        }
        return null;
    }
}