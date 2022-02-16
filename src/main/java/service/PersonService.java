package service;

import entity.Person;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class PersonService {

    public Person savePerson(Session session, String name) {
        Person person = findPerson(session, name);

        if (person == null) {
            person = new Person();
            person.setName(name);
            session.persist(person);
        }

        return person;
    }

    private Person findPerson(Session session, String name) {
        Query<Person> query = session.createQuery("select p from Person p where p.name=:name", Person.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }
}
