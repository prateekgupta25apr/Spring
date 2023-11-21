package prateek_gupta;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

public class DAOImpl implements DAO{

    @Autowired
    SessionFactory factory;
    @Override
    public String save(Table1Entity entity) {

        Session session=null;
        try{
            session= factory.openSession();
            Transaction transaction= session.beginTransaction();
            session.save(entity);
            transaction.commit();
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
        return "Monuments details saved";
    }
}
