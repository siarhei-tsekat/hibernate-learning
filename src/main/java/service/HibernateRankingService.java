package service;

import entity.Person;
import entity.Ranking;
import entity.Skill;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.SessionUtil;

import java.util.List;
import java.util.stream.Collectors;

public class HibernateRankingService implements RankingService {

    @Override
    public int getRanking(String subjectName, String skillCaption) {

        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            int average = getAvgRankingFor(subjectName, skillCaption, session);
            transaction.commit();
            return average;
        }
    }

    @Override
    public void addRanking(String subjectName, String observerName, String skill, int ranking) {

        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            addRankingFor(subjectName, observerName, skill, ranking, session);
            transaction.commit();
        }
    }

    @Override
    public void updateRanking(String subjectName, String observerName, String skillCaption, int rank) {
        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();

            Ranking ranking = getRankingFor(subjectName, observerName, skillCaption, session);
            if (ranking == null) {
                addRankingFor(subjectName, observerName, skillCaption, rank, session);
            } else {
                ranking.setRanking(rank);
            }
            transaction.commit();
        }
    }

    @Override
    public void removeRanking(String subjectName, String observerName, String skillCaption) {
        try (Session session = SessionUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            Ranking ranking = getRankingFor(subjectName, observerName, skillCaption, session);

            if (ranking != null) {
                session.remove(ranking);
            }

            transaction.commit();
        }
    }

    private void addRankingFor(String subjectName, String observerName, String skill, int ranking, Session session) {
        Person subjectEntity = savePerson(subjectName, session);
        Person observerEntity = savePerson(observerName, session);
        Skill skillEntity = saveSkill(skill, session);

        Ranking rankingEntity = new Ranking();
        rankingEntity.setSubject(subjectEntity);
        rankingEntity.setObserver(observerEntity);
        rankingEntity.setSkill(skillEntity);
        rankingEntity.setRanking(ranking);

        session.persist(rankingEntity);
    }

    private Skill saveSkill(String skillCaption, Session session) {
        Skill skill = findSkill(skillCaption, session);

        if (skill == null) {
            skill = new Skill();
            skill.setCaption(skillCaption);
            session.persist(skill);
        }
        return skill;
    }

    private Skill findSkill(String skillCaption, Session session) {
        Query<Skill> query = session.createQuery("select s from Skill s where s.caption=:skillCaption", Skill.class);
        query.setParameter("skillCaption", skillCaption);
        return query.uniqueResult();
    }

    private Person savePerson(String subjectName, Session session) {
        Person person = findPerson(subjectName, session);
        if (person == null) {
            person = new Person();
            person.setName(subjectName);
            session.persist(person);
        }
        return person;
    }

    private Person findPerson(String subjectName, Session session) {
        Query<Person> query = session.createQuery("select p from Person p where p.name=:personName", Person.class);
        query.setParameter("personName", subjectName);
        return query.uniqueResult();
    }

    private int getAvgRankingFor(String subjectName, String skillCaption, Session session) {
        Query<Ranking> query = session.createQuery("select r from Ranking r where r.subject.name=:subjectName and r.skill.caption=:skillCaption", Ranking.class);
        query.setParameter("subjectName", subjectName);
        query.setParameter("skillCaption", skillCaption);
        List<Ranking> list = query.list();

        double average = list.stream()
                .collect(Collectors.summarizingInt(Ranking::getRanking))
                .getAverage();

        return (int) average;
    }

    private Ranking getRankingFor(String subjectName, String observerName, String skillCaption, Session session) {
        Query<Ranking> query = session.createQuery("select r from Ranking r where r.subject.name=:subjectName and r.observer.name=:observerName and r.skill.caption=:skillCaption", Ranking.class);
        query.setParameter("subjectName", subjectName);
        query.setParameter("observerName", observerName);
        query.setParameter("skillCaption", skillCaption);
        return query.uniqueResult();
    }
}
