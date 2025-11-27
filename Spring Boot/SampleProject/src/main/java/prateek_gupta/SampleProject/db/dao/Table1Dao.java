package prateek_gupta.SampleProject.db.dao;

import prateek_gupta.SampleProject.db.entities.Table1Entity;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

public interface Table1Dao {
    void saveData(Table1Entity table1Entity) throws ServiceException;

    void partialUpdateData(Integer primaryKey,String col1,Boolean col2) throws ServiceException;
}
