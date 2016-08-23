package Calculator;
import Database.Entry;
import Database.Content;
import java.util.ArrayList;

public class Timetable {
    private ArrayList<Entry> entries;
    private ArrayList<Content> contents;
    private int generation;

    public Timetable(){
        this.entries = new ArrayList<>();
        this.contents = new ArrayList<>();
        this.generation = 1;
    }

    public ArrayList<Content> getContents(){
        return this.contents;
    }
    public ArrayList<Entry> getEntries(){
        return this.entries;
    }
    public int getGeneration() { return this.generation; }

    public void addContent(Content content){
        this.contents.add(content);
    }
    public void addEntry(Entry entry){
        this.entries.add(entry);
    }
    public void reset(){
        this.contents.removeAll(this.contents);
        this.entries.removeAll(this.entries);
        this.generation++;
    }

    public int findMinSlot(Lesson lesson, int algorithm, int even, int slotsUsed){
        try {
            if (lesson.getType()==0) return 0;
            if (algorithm==3) return 43;
            if(algorithm==1){
                if(even <= slotsUsed) return 43;
            }
//            for (Entry entry: entries) {
//                if(entry.getGroup()==lesson.getGroups().get(0).getId() && entry.getCourse()==lesson.getCourse() && entry.getContent().getType() == 0) return entry.getContent().getSlot() + 1;
//            }
            return 0;
        } catch (Exception e){
            Run.saveStatus(414);
        }
        return 0;
    }
}