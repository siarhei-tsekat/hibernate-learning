import entity.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.Test;

public class Initial {

    @Test
    public void test1() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        Message message = new Message();
        message.setText("Hello world");


        try (Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();
            session.persist(message);
            transaction.commit();
        }
    }
}
