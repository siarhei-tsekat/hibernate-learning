import entity.Person;
import entity.Ranking;
import entity.Skill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.PersonService;
import service.SkillService;

import java.util.List;
import java.util.stream.Collectors;

public class RankingTest {

    private SessionFactory sessionFactory;
    private PersonService personService;
    private SkillService skillService;

    @BeforeClass
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        personService = new PersonService();
        skillService = new SkillService();
    }

    @Test
    public void testRankings() {
        populateRankingData("Bob", "Sara", "Java", 9);
        populateRankingData("Bob", "Nikita", "Java", 6);
        populateRankingData("Bob", "Mike", "Java", 0);

        try (Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();
            String select = "select r from Ranking r where r.subject.name=:subjectName and r.skill.caption=:skillCaption";
            Query<Ranking> query = session.createQuery(select, Ranking.class);
            query.setParameter("subjectName", "Bob");
            query.setParameter("skillCaption", "Java");
            List<Ranking> list = query.list();
            Double res = list.stream().collect(Collectors.averagingInt(Ranking::getRanking));
            Assert.assertEquals(res, 5.0);


            transaction.commit();
        }
    }

    @Test(dependsOnMethods = "testRankings")
    public void saveRanking() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Person subject = personService.savePerson(session, "Bob");
        Person observer = personService.savePerson(session, "Sara");
        Skill skill = skillService.saveSkill(session, "Java");

        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(8);

        session.persist(ranking);

        transaction.commit();

    }

    @Test(dependsOnMethods = "testRankings")
    public void changeRanking() {
        populateRankingData("Mike", "Sara", "C++", 4);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String s = "select r from Ranking r where r.subject.name=:subjectName and r.observer.name=:observerName and r.skill.caption=:skillCaption";
            Query<Ranking> query = session.createQuery(s, Ranking.class);
            query.setParameter("subjectName", "Mike");
            query.setParameter("observerName", "Sara");
            query.setParameter("skillCaption", "C++");

            Ranking ranking = query.uniqueResult();
            Assert.assertNotNull(ranking);
            ranking.setRanking(8);

            transaction.commit();
        }

    }

    @Test(dependsOnMethods = "testRankings")
    public void removeRanking() {
        populateRankingData("Niko", "Sam", "C#", 4);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Ranking> query = session.createQuery("select r from Ranking r where r.subject.name=:subjectName", Ranking.class);
            query.setParameter("subjectName", "Niko");
            Ranking ranking = query.uniqueResult();

            session.delete(ranking);

            transaction.commit();
        }

    }

    @AfterClass
    public void tearDown() {
        sessionFactory.close();
    }

    private void populateRankingData(String subjectName, String observerName, String skillCaption, int rankingValue) {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Person subject = personService.savePerson(session, subjectName);
        Person observer = personService.savePerson(session, observerName);
        Skill skill = skillService.saveSkill(session, skillCaption);

        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(rankingValue);

        session.persist(ranking);

        transaction.commit();
    }

}
