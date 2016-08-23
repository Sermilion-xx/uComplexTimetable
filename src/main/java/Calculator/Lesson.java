package Calculator;

import Database.Group;
import java.util.ArrayList;
import java.util.List;

public class Lesson implements Comparable<Lesson>{

    private final int course;
    private final int teacher;
    private final int hours;
    private final int roomType;
    private final int type;
    private List<Group> groups;
    private final int subgroup;

    public Lesson(int course, int teacher, int hours, int roomType, int type, int subgroup){
        this.course = course;
        this.teacher = teacher;
        this.hours = hours;
        this.roomType = roomType;
        if (type == 0 || type == 1 || type == 2) this.type = type;
        else {
            this.type = 0;
            Run.saveStatus(415);
        }
        this.subgroup = subgroup;
        this.groups = new ArrayList<>();
    }

    public int getCourse() { return this.course; }
    public int getTeacher() { return this.teacher; }
    public int getHours() { return this.hours; }
    public int getRoomType() { return this.roomType; }
    public int getType(){ return this.type; }
    public int getSubgroup() { return  this.subgroup; }

    public List<Group> getGroups() { return this.groups; }

    public void addGroup(Group group){
        if(!this.getGroups().contains(group)) this.groups.add(group);
    }



    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Lesson other){
        Integer this_course = this.getCourse();
        Integer that_course = other.getCourse();
        Integer this_type = this.getType();
        Integer that_type = other.getType();
        int i = this_course.compareTo(that_course);
        if(i != 0) return i;
        return this_type.compareTo(that_type);
    }
}
