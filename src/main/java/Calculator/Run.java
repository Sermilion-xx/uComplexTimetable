package Calculator;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.sql.*;
import Database.Database;
import Database.DatabaseJDBC;

public class Run{
    private static int tableId;
    public static int breakTime;
    public static int algorithm;

    public static void saveStatus(int status){
        System.out.println("STATUS " + status);
        DatabaseJDBC db = new DatabaseJDBC();
        String sql = "UPDATE up_tables SET calc_status=" + status + " WHERE id=" + Run.tableId;
        try {
            db.runSql(sql);
        } catch (SQLException e){
            System.exit(0);
        }
        System.exit(0);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Thread.UncaughtExceptionHandler h = (th, ex) -> saveStatus(502);
        Thread t = new Thread() {
            public void run() {
                try {
                    tableId = Integer.valueOf(args[0]);
                    algorithm = Integer.valueOf(args[1]);
                    breakTime = Integer.valueOf(args[2]);
                    breakTime = 3;
                } catch (Exception e){
                    saveStatus(501);
                }

                try {
                    new ServerSocket(1612);
                    DatabaseJDBC dbj = new DatabaseJDBC();
                    String sql = "UPDATE up_tables SET calc_started_at=CURRENT_TIMESTAMP, calc_status=1 WHERE id=" + tableId;
                    try {
                        dbj.runSql(sql);
                    } catch (SQLException e){
                        saveStatus(401);
                    }
                    Database db = new Database();
                    TimetableCalculator calculator = new TimetableCalculator(db, tableId);
                    Timetable timetable = calculator.calculate(1000, algorithm);
                    System.out.println("Number of contents created: " + timetable.getContents().size());
                    System.out.println("Number of entries created: " + timetable.getEntries().size());
                    System.out.println("Tried: " + timetable.getGeneration());

                    if(timetable.getContents().size() > 0 && timetable.getEntries().size() > 0) {
                        try {
                            sql = dbj.makeContentSaveStatement(timetable);
                            dbj.runSql(sql);
                        } catch (SQLException e) {
                            saveStatus(497);
                        }
                        try {
                            sql = dbj.makeEntrySaveStatement(timetable);
                            dbj.runSql(sql);
                            saveStatus(200);
                            System.out.println("Расписание занятий сохранено в БД");
                        } catch (SQLException e) {
                            saveStatus(497);
                        }
                    } else{
                        saveStatus(500);
                    }
                    System.exit(0);
                } catch (BindException e) {
                     saveStatus(300);
                } catch (IOException e) {
                    System.exit(0);
                }
                catch (Exception e){
                    saveStatus(498);
                }
            }
        };
        t.setUncaughtExceptionHandler(h);
        t.start();
    }
}

// todo Analyse load
// todo breakTime fix
// todo Save GroupCourses append if (group, course, table) is there