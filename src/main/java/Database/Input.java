package Database;
import org.hibernate.HibernateException;
import java.util.List;

public interface Input {
    Table getTable(int id) throws HibernateException;

    List<Group> getGroups(List<Integer> groupIds) throws HibernateException;

    List<Room> getRooms() throws HibernateException;

    List<Load> getLoads(int tableId) throws HibernateException;

    List<Object[]> getEntries(List<Integer> days, int folder) throws HibernateException;

    int getMaxContentId() throws HibernateException;

    int getMaxEntryId() throws HibernateException;

    void closeSession();
}