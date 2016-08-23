package Database;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class Database implements Input {

    private Session session;
    private SessionFactory sessionFactory;

    public Database(){
        Configuration configuration = new Configuration();
        configuration.configure();
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        this.session = sessionFactory.openSession();
        this.session.beginTransaction();
        System.out.println("Начинаю загрузку данных с сервера...");
    }

    public void closeSession(){
        session.getTransaction().commit();
        this.session.close();
        this.sessionFactory.close();
        System.out.println("Соединение с базой данных закрыто.");
    }

    public Table getTable(int id) throws HibernateException{
        Table table = (Table) session.createQuery("from Table where id=:id").setParameter("id", id).uniqueResult();
        System.out.println("Загружен шаблон расписания " + table.getName());
        return table;
    }

    public List<Group> getGroups(List<Integer> groupIds) throws HibernateException{
        @SuppressWarnings("unchecked")
        List<Group> groups = session.createQuery("from Group g WHERE g.id IN :groups").setParameterList("groups", groupIds).list();
        System.out.println("Добавлено " + groups.size() + " групп.");
        return groups;
    }

    public List<Room> getRooms() throws HibernateException{
        @SuppressWarnings("unchecked")
        List<Room> rooms = session.createQuery("from Room ").list();
        System.out.println("Добавлено " + rooms.size() + " аудиторий.");
        return rooms;
    }

    public List<Load> getLoads(int tableId) throws HibernateException{
        @SuppressWarnings("unchecked")
        List<Load> loads = session.createQuery("select d from Load d JOIN d.group g Join d.table t WHERE t.id = :tableId")
                .setParameter("tableId", tableId).list();
        System.out.println("Добавлено " + loads.size() + " учебных нагрузок.");
        return loads;
    }

    public List<Object[]> getEntries(List<Integer> days, int folder) throws HibernateException{
        @SuppressWarnings("unchecked")
        List<Object[]> entries = session.createQuery("SELECT e,c FROM Entry e JOIN e.content c JOIN c.table t JOIN  c.group WHERE c.day IN :days and t.folder = :folder").setParameterList("days", days).setParameter("folder", folder).list();
        System.out.println("Добавлено " + entries.size() + " энтрис.");
        return entries;
    }

    public int getMaxContentId(){
        Object mid = session.createQuery("select max(id) as mid from Content ").uniqueResult();
        if(mid == null) return 1;
        else return (int) mid + 1;
    }

    public int getMaxEntryId(){
        Object mid = session.createQuery("select max(id) as mid from Entry ").uniqueResult();
        if(mid == null) return 1;
        else return (int) mid + 1;
    }

}