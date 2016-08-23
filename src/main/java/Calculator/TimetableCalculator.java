package Calculator;
import Database.Input;
import Database.Table;
import Database.*;
import com.rits.cloning.Cloner;
import org.hibernate.HibernateException;
import java.util.*;

public class TimetableCalculator{

    private Table table;
    private Faculty faculty;
    public static int building;
    private List<Group> groups;
    private List<Room> rooms;
    private List<Room> roomsFaculty;
    private List<Room> roomsBuilding;
    private int roomLargestCapacity;
    private List<Load> loads;
    private List<Lesson> lessons;
    private Allocation allocation;
    private int maxContentId;
    private int maxEntryId;
    private List<Integer> slots;
    private int lessonsTotal;

    public TimetableCalculator(Input input, int tableId) {
        try {
            this.table = input.getTable(tableId);
        } catch (Exception e) {
            Run.saveStatus(402);
        }
        try {
            this.faculty = this.table.getFaculty();
            building = this.faculty.getBuilding();
        } catch (Exception e) {
            Run.saveStatus(403);
        }
        try {
            this.groups = input.getGroups(this.table.getGroupsNative());
            if(this.groups.size() < 1) throw new Exception();
        } catch (Exception e) {
            Run.saveStatus(404);
        }
        try {
            this.rooms = input.getRooms();
        } catch (Exception e) {
            Run.saveStatus(405);
        }
        try {
            this.roomsFaculty = Utils.sortRooms(Utils.getRoomsByFaculty(this.rooms, this.faculty));
            this.roomsBuilding = Utils.sortRooms(Utils.getRoomsByBuilding(this.rooms, this.faculty));
            this.roomLargestCapacity = this.roomsBuilding.get(this.roomsBuilding.size()-1).getSeats();
        } catch (Exception e){
            Run.saveStatus(406);
        }
        try {
            this.loads = input.getLoads(tableId);
        } catch (Exception e) {
            Run.saveStatus(407);
        }
        try {
            List<Object[]> entries = input.getEntries(table.getDaysNative(), table.getFolder());
            this.allocation = Utils.makeAllocation(entries, this.groups);
        }
        catch (HibernateException e) {
            Run.saveStatus(409);
            System.exit(0);
        } catch (Exception e) {
            Run.saveStatus(410);
        }
        try {
            this.lessons = Utils.makeLessons(this.loads, this.getRoomLargestCapacity(), this.allocation);
            if(this.lessons.size() < 1) Run.saveStatus(200);
            this.lessonsTotal = lessons.stream().mapToInt(Lesson::getHours).sum();
        } catch (Exception e) {
            Run.saveStatus(408);
        }
        try{
            this.maxContentId = input.getMaxContentId();
            this.maxEntryId = input.getMaxEntryId();
        } catch (HibernateException e){
            Run.saveStatus(411);
        } catch (Exception e){
            Run.saveStatus(412);
        }
        try {
            this.slots = Utils.getTableSlots(this.table.getHours());
        } catch (Exception e){
            Run.saveStatus(413);
        }
        input.closeSession();
        System.out.println("Все данные загружены.");
    }

    public int getMaxContentId(){
        return this.maxContentId;
    }
    public int getMaxEntryId(){
        return this.maxEntryId;
    }
    public void setMaxContentId(){
        this.maxContentId++;
    }
    public void setMaxEntryId(){
        this.maxEntryId++;
    }
    public int getRoomLargestCapacity() { return this.roomLargestCapacity;}

    public Room assignRoom(Lesson lesson, Allocation allocation, int slot) {
        Room room = Utils.pickRoom(roomsFaculty, lesson, allocation, slot);
        if (room != null) return room;
        else {
            room = Utils.pickRoom(roomsBuilding, lesson, allocation, slot);
            if (room != null) return room;
            else return null;
        }
    }

    private void mutate() {
        Random rand = new Random();
        int index1 = rand.nextInt(lessons.size());
        int index2 = rand.nextInt(lessons.size());
        lessons.set(index1, lessons.get(index2));
        lessons.set(index2, lessons.get(index1));
    }

    public Timetable calculate(int generations, int algorithm) {
        int fitness = 1;
        Cloner cloner = new Cloner();
        Allocation $allocation = cloner.deepClone(this.allocation);
        Timetable timetable = new Timetable();
        int check_fitness = 0;
        int even;

        try {
            while (fitness > 0 && generations > 0) {
                for (Lesson lesson : lessons) {
                    even = 0;
                    if(lesson.getType()==1 || lesson.getType()==2) even = Utils.getGroupHoursAverage(lessons, lesson);
                    int minSlot = timetable.findMinSlot(lesson, algorithm, even, check_fitness);
                    for (int i = 0; i < lesson.getHours(); i++) {
                        slotLoop:
                        for (int slot : slots) {
                            if (slot < minSlot) continue;
                            for (Group _group: lesson.getGroups()) if (!$allocation.isGroupAvailable(_group.getId(), slot, lesson.getSubgroup())) continue slotLoop;
                            if (!$allocation.isTeacherAvailable(lesson.getTeacher(), slot)) continue ;
                            Room room = this.assignRoom(lesson, $allocation, slot);
                            if (room == null) continue;
                            Boolean pg;
                            for (Group _group: lesson.getGroups()) {
                                pg = $allocation.putGroupAllocation(_group.getId(), slot, lesson.getSubgroup());
                                if (!pg) continue slotLoop;
                            }
                            if (!$allocation.putTeacherAllocation(lesson.getTeacher(), slot, this.faculty.getBuilding())) continue;
                            if (!$allocation.putRoomAllocation(room.getId(), slot)) continue;

                            int[] time = Utils.slotToNativeTime(slot);
                            for(Group _group: lesson.getGroups()){
                                if(_group==null) break;
                                Content content = new Content(this.getMaxContentId(), time[0], time[1], time[2], lesson.getType(), _group, table, slot);
                                this.setMaxContentId();
                                Entry entry = new Entry(this.getMaxEntryId(), lesson.getTeacher(), lesson.getCourse(), room, lesson.getSubgroup(), content, _group, table);
                                this.setMaxEntryId();
                                timetable.addContent(content);
                                timetable.addEntry(entry);
                            }
                            check_fitness++;
                            break;
                        }
                    }
                }

                fitness = lessonsTotal - check_fitness;
                System.out.println("Current Fitness: " + fitness);

                if (fitness < 1) return timetable;

                timetable.reset();
                check_fitness = 0;
                $allocation = cloner.deepClone(this.allocation);
                this.mutate();
                generations--;
            }
        } catch (Exception e){
            Run.saveStatus(499);
        }
        return timetable;
    }
}