package prateek_gupta.SampleProject.db.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import prateek_gupta.SampleProject.db.dao.Table1Dao;
import prateek_gupta.SampleProject.db.entities.Table1Entity;
import prateek_gupta.SampleProject.db.service.impl.DBServiceImpl;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
@Transactional
public class Table1DaoImpl implements Table1Dao {

    private final Logger log = LoggerFactory.getLogger(Table1DaoImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void saveData(Table1Entity table1Entity) throws ServiceException {
        log.info("Entering Service : saveData()");
        try{
            entityManager.merge(table1Entity);
        }catch (Exception e){
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : saveData()");
    }

    @Override
    public void partialUpdateData(Integer primaryKey,String col1,Boolean col2)
            throws ServiceException {
        log.info("Entering Service : partialUpdateData()");
        try{
            String setCols="";

            if (StringUtils.isNotBlank(col1))
                setCols="col_1 = :col1";

            if (col2!=null){
                if (StringUtils.isNotBlank(setCols))
                    setCols+=" , ";
                setCols+=" col_2 = :col2";
            }

            String updateQuery = "update table_1 set " + setCols + " where primary_key = "
                    + primaryKey;

            Query query=entityManager.createNativeQuery(updateQuery);
            if (StringUtils.isNotBlank(col1))
                query.setParameter("col1", col1);

            if (col2 != null)
                query.setParameter("col2", col2);

            query.executeUpdate();
        }catch (Exception e){
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : partialUpdateData()");
    }
}
