package org.prateekgupta.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.prateekgupta.entity.MobileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;

@Component
public class MobileDAOImpl implements MobileDAO{

    @Autowired
    SessionFactory factory;

    @Override
    public String save(MobileEntity entity) {
        Session session=null;
        try{
            session= factory.openSession();
            Transaction transaction= session.beginTransaction();
            session.save(entity);
            transaction.commit();
        }
        catch (PersistenceException e){
            return "Details of the mobile not saved";
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
        return "Details of the mobile saved";
    }
}
