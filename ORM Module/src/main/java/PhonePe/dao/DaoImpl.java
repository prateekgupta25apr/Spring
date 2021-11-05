package PhonePe.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import PhonePe.entity.LoginEntity;
import PhonePe.entity.UserEntity;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoImpl implements Dao {
    @Override
    public String save(UserEntity entity) {
        Session session = null;
        SessionFactory factory;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "phonepe.xml").getBean("factory");
            session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return "Details of the user saved";
    }

    @Override
    public UserEntity getById(int id) {
        Session session = null;
        SessionFactory factory;
        UserEntity result;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "phonepe.xml").getBean("factory");
            session = factory.openSession();
            Query query=session.createNamedQuery("getById");
            query.setParameter("providedId",id);
            result =(UserEntity) query.uniqueResult();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    @Override
    public LoginEntity getByAccountNumber(int accountNumber) {
        Session session = null;
        SessionFactory factory;
        LoginEntity result;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "phonepe.xml").getBean("factory");
            session = factory.openSession();
            Query query=session.createNamedQuery("getByAccountNumber");
            query.setParameter("providedAccountNumber",accountNumber);
            result= (LoginEntity) query.uniqueResult();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    @Override
    public String updatePassword(String password, int id) {
        Session session = null;
        SessionFactory factory;
        UserEntity result;
        try {
            factory = (SessionFactory) new ClassPathXmlApplicationContext(
                    "phonepe.xml").getBean("factory");
            session = factory.openSession();
            Transaction transaction= session.beginTransaction();
            Query query=session.createNamedQuery("updatePassword");
            query.setParameter("providedPassword",password);
            query.setParameter("providedId",id);
            query.executeUpdate();
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return "Password updated";
    }
}
