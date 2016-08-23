package Database;

public class Load {
    private int id;
    private int course;
    private int teacher;
    private int lecture;
    private int seminar;
    private int lab;
    private int lectureRoom;
    private int seminarRoom;
    private int labRoom;
    private int subgroup;
    private Group group;
    private Table table;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(int subgroup) {
        this.subgroup = subgroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getLecture() {
        return lecture;
    }

    public void setLecture(int lecture) {
        this.lecture = lecture;
    }

    public int getSeminar() {
        return seminar;
    }

    public void setSeminar(int seminar) {
        this.seminar = seminar;
    }

    public int getLab() {
        return lab;
    }

    public void setLab(int lab) {
        this.lab = lab;
    }

    public int getLectureRoom() {
        return lectureRoom;
    }

    public void setLectureRoom(int lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    public int getSeminarRoom() {
        return seminarRoom;
    }

    public void setSeminarRoom(int seminarRoom) {
        this.seminarRoom = seminarRoom;
    }

    public int getLabRoom() {
        return labRoom;
    }

    public void setLabRoom(int labRoom) {
        this.labRoom = labRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Load load = (Load) o;

        return id == load.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
