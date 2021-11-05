package Planet.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import Planet.entity.PlanetEntity;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DAOImpl implements Planet.dao.DAO {
    @Override
    public String save(PlanetEntity entity) {
        Session session = null;
        SessionFactory factory;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "planet.xml").getBean("factory");
            session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return "Details of the planet saved";
    }

    @Override
    public PlanetEntity getById(int id) {
        Session session = null;
        SessionFactory factory;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "planet.xml").getBean("factory");
            session = factory.openSession();
            return session.get(PlanetEntity.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public String update(PlanetEntity entity) {
        Session session = null;
        SessionFactory factory;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "planet.xml").getBean("factory");
            session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery(
                    "update PlanetEntity set name = '" +
                    entity.getName() + "' where id=" + entity.getId());
            query.executeUpdate();
            System.out.println(session.get(PlanetEntity.class,1));
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return "Details of the planet updated";
    }

    @Override
    public String delete(int id) {
        Session session = null;
        SessionFactory factory;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "planet.xml").getBean("factory");
            session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            PlanetEntity entity = session.get(PlanetEntity.class, id);
            session.delete(entity);
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return "Details of the planet deleted";
    }
}
