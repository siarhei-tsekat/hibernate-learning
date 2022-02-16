package service;

import entity.Skill;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class SkillService {

    public Skill saveSkill(Session session, String caption) {
        Skill skill = findSkill(session, caption);
        if (skill == null) {
            skill = new Skill();
            skill.setCaption(caption);
            session.persist(skill);
        }
        return skill;
    }

    private Skill findSkill(Session session, String caption) {

        Query<Skill> query = session.createQuery("select s from Skill s where s.caption=:caption", Skill.class);
        query.setParameter("caption", caption);
        return query.uniqueResult();
    }

}
