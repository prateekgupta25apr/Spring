package Butterfly.dao;

import Butterfly.entity.ButterflyEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DAOImpl implements DAO{
	
    @Override
    public String save(ButterflyEntity entity) {
        Session session = null;
        SessionFactory factory;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "butterfly.xml").getBean("factory");
            session = factory.openSession();
            Transaction transaction= session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return "Details of the butterfly saved";
    }

    @Override
    public ButterflyEntity getById(int id) {
        Session session = null;
        SessionFactory factory;
        ButterflyEntity result;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "butterfly.xml").getBean("factory");
            session = factory.openSession();
            result =session.get(ButterflyEntity.class,id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }
}
