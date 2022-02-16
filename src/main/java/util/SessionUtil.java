package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SessionUtil {
    private static SessionUtil instance = new SessionUtil();
    private SessionFactory sessionFactory;

    public static Session openSession() {
        return getInstance().sessionFactory.openSession();
    }

    public static void forceReload() {
        getInstance().initialize();
    }

    public static SessionUtil getInstance() {
        return instance;
    }

    private void initialize() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private SessionUtil() {
        initialize();
    }

}
