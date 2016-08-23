package Database;
import Calculator.Run;
import Calculator.Timetable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseJDBC {
    private Connection conn = null;

    public DatabaseJDBC() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ucomplex_dev?user=ucremote&password=nBTrtcyKuHSBpVQc");
        } catch (Exception e) {
            Run.saveStatus(400);
        }
    }

    public boolean runSql(String sql) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.execute(sql);
    }

    public String makeContentSaveStatement(Timetable timetable){
        String sql = "INSERT INTO up_tables_content (id, `group`, `week`, `day`, `hour`, `type`, `table`) values ";
        for (Content content : timetable.getContents()) {
            sql = sql + "(" + content.getId() + "," + content.getGroup().getId() + ", " + content.getWeek() + ", " + content.getDay() + ", " + content.getHour() + ", " + content.getType() + ", " + content.getTable().getId() + "),";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + ";";
        return sql;
    }

    public String makeEntrySaveStatement(Timetable timetable){
        String sql = "INSERT INTO up_timetable_entries (id, `group`, `teacher`, `course`, `room`, `p`, `content`, `table`) values ";
        for (Entry entry : timetable.getEntries()) {
            sql = sql + "(" + entry.getId() + ", " + entry.getGroup() + ", " + entry.getTeacher() + ", " + entry.getCourse() + ", " + entry.getRoom().getId() + ", " + entry.getP() + ", " + entry.getContent().getId() + ", " + entry.getTable() + "),";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + ";";
        return sql;
    }
}
