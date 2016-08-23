package Database;

/**
 * Created by Ruslan Batukaev on 26/03/15.
 * Entry Class
 */

public class Entry {
    private int id;
    private int teacher;
    private int course;
    private Room room;
    private int p;
    private Content content;
    private int group;
    private int table;

    public Entry(){

    }

    public Entry(int id, int teacher, int course, Room room, int p, Content content, Group group, Table table){
        this.id = id;
        this.teacher = teacher;
        this.course = course;
        this.room = room;
        this.p = p;
        this.content = content;
        this.group = group.getId();
        this.table = table.getId();
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getP() {
        return this.p;
    }

    public void setP(int p) {
        this.p = p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        if (course != entry.course) return false;
        if (id != entry.id) return false;
        if (p != entry.p) return false;
        if (room != entry.room) return false;
        if (teacher != entry.teacher) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + teacher;
        result = 31 * result + course;
//        result = 31 * result + room;
        result = 31 * result + p;
        return result;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
