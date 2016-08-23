package Database;
import java.util.List;


public class Content {
    private int id;
    private int week;
    private int day;
    private int hour;
    private int type;
    private Group group;
    private Table table;
    private List<Entry> entries;
    private int slot;

    public Content(){

    }

    public Content(int id, int w, int d, int h, int type, Group group, Table table, int slot){
        this.id = id;
        this.week = w;
        this.day = d;
        this.hour = h;
        this.type = type;
        this.group = group;
        this.table = table;
        this.slot = slot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSlot() { return this.slot; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;

        return day == content.day && hour == content.hour && id == content.id && type == content.type && week == content.week;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + week;
        result = 31 * result + day;
        result = 31 * result + hour;
        result = 31 * result + type;
        return result;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }


}
