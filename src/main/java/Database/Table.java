package Database;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private int id;
    private String name;
    private String hours;
    private String days;
    private String groups;
    private int status;
    private Faculty faculty;
    private List<Content> contents;
    private int folder;

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public List<Integer> getGroupsNative() {
        ArrayList<Integer> result = new ArrayList<>();
        String[] list = this.groups.split(",");
        for(String s: list){
            result.add(Integer.valueOf(s));
        }
        return result;
    }

    public List<Integer> getDaysNative(){
        ArrayList<Integer> result = new ArrayList<>();
        String[] list = this.days.split(",");
        for(String s: list){
            result.add(Integer.valueOf(s));
        }
        return result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        return id == table.id && status == table.status && !(days != null ? !days.equals(table.days) : table.days != null) && !(groups != null ? !groups.equals(table.groups) : table.groups != null) && !(hours != null ? !hours.equals(table.hours) : table.hours != null) && !(name != null ? !name.equals(table.name) : table.name != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (hours != null ? hours.hashCode() : 0);
        result = 31 * result + (days != null ? days.hashCode() : 0);
        result = 31 * result + (groups != null ? groups.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }
}
