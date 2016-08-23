package Calculator;

import java.util.*;

public class Allocation {

    private HashMap<Integer,HashSet<Integer>> room_alloc;
    private HashMap<Integer,HashMap<Integer, HashSet<Integer>>> group_alloc;
    private HashMap<Integer, HashSet<Integer>> teacher_alloc;
    private HashMap<Integer, HashMap<Integer, Integer>> teacher_building_alloc;
    private HashMap<String, Integer> allocated_load;

    public Allocation(){
        this.room_alloc = new HashMap<>();
        this.group_alloc = new HashMap<>();
        this.teacher_alloc = new HashMap<>();
        this.teacher_building_alloc = new HashMap<>();
        this.allocated_load = new HashMap<>();
    }

    public HashMap<String, Integer> getAllocated_load(){ return this.allocated_load; }
    public void putAllocatedLoad(String index){
        if (this.allocated_load.get(index) == null) this.allocated_load.put(index, 0);
        int hours = this.allocated_load.get(index);
        this.allocated_load.put(index, hours + 1);
    }
    public int getHoursToAllocate(String index, int hours){
        if (this.allocated_load.get(index) == null) this.allocated_load.put(index, 0);
        return hours - this.allocated_load.get(index);
    }

    public Boolean isGroupAvailable(Integer group, int slot, int subgroup){
        HashMap<Integer, HashSet<Integer>> group_allocations = this.group_alloc.get(group);
        if (group_allocations == null) return true;
        HashSet<Integer> slotsTemp = new HashSet<>();
        if(subgroup == -1){
            slotsTemp = new HashSet<>();
            for(Map.Entry<Integer, HashSet<Integer>> entry: this.group_alloc.get(group).entrySet()){
                slotsTemp.addAll(entry.getValue());
            }
        } else {
            if(this.group_alloc.get(group).get(subgroup) != null) slotsTemp.addAll(this.group_alloc.get(group).get(subgroup));
            if(this.group_alloc.get(group).get(-1) != null) slotsTemp.addAll(this.group_alloc.get(group).get(-1));
        }
        if(slotsTemp.isEmpty()) return true;
        else {
            for(int slot_:slotsTemp){
                if(slot_ == slot) return false;
            }
            return dayLoadCheck(slotsTemp, slot, 4);
        }
    }
    public Boolean isTeacherAvailable(int teacher, int slot){
        HashSet<Integer> slots = this.teacher_alloc.get(teacher);
        if(slots == null || slots.isEmpty()) return true;
        else {
            for(int slot_:slots){
                if(slot_ == slot) return false;
            }
            if(!dayLoadCheck(slots, slot, 3)) return false;
            return checkTeacherBuilding(slot, teacher);
        }
    }
    public Boolean isRoomAvailable(Integer room, int slot){
        Set<Integer> slots = this.room_alloc.get(room);
        if(slots == null || slots.isEmpty()) return true;
        else {
            for(int slot_:slots){
                if(slot_ == slot) return false;
            }
            return true;
        }
    }


    public Boolean putRoomAllocation(Integer room, int slot){
        HashSet<Integer> slots = this.room_alloc.get(room);
        if(slots == null) {
            slots = new HashSet<>();
        }
        slots.add(slot);
        this.room_alloc.put(room, slots);
        return true;
    }

    public Boolean putGroupAllocation(Integer group, int slot, int subgroup){
        if(this.group_alloc.get(group) == null){
            this.group_alloc.put(group, new HashMap<>());
        }
        HashSet<Integer> slots = this.group_alloc.get(group).get(subgroup);
        if(slots == null) {
            this.group_alloc.get(group).put(subgroup, new HashSet<>());
            slots = this.group_alloc.get(group).get(subgroup);
        }
        slots.add(slot);
        this.group_alloc.get(group).put(subgroup, slots);
        return true;
    }
    public Boolean putTeacherAllocation(int teacher, int slot, int building){
        HashSet<Integer> slots = this.teacher_alloc.get(teacher);
        if(slots == null) {
            slots = new HashSet<>();
        }
        slots.add(slot);
        this.teacher_alloc.put(teacher, slots);
        if(this.teacher_building_alloc.get(teacher)==null){
            this.teacher_building_alloc.put(teacher, new HashMap<>());
        }
        this.teacher_building_alloc.get(teacher).put(slot, building);
        return true;
    }

    public Boolean dayLoadCheck(HashSet<Integer> slots, int slot, int allowed){
        int offset = slot % 6;
        if (offset == 0) offset = 6;
        int start = slot - offset + 1;
        int j = 0;
        for (int i = start; i < start + 6; i++){
            if (slots.contains(i)) j++;
        }
        return j < allowed;
    }

    public Boolean checkTeacherBuilding(int slot, int teacher){
        int checkSlot;
        if(slot==1 || (slot-1) % Run.breakTime == 0) return true;
        if(slot % Run.breakTime == 0) checkSlot = slot - 1;
        else checkSlot = slot + 1;
        HashMap<Integer, Integer> teacher_building_alloc = this.teacher_building_alloc.get(teacher);
        if (teacher_building_alloc != null && teacher_building_alloc.get(checkSlot) != null) if(teacher_building_alloc.get(checkSlot) != TimetableCalculator.building) return false;
        return true;
    }
}
