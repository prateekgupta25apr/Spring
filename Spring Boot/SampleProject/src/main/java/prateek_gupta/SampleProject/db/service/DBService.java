package prateek_gupta.SampleProject.db.service;

import prateek_gupta.SampleProject.db.vo.Table1VO;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

public interface DBService {
    Table1VO getData(Integer primaryKey) throws ServiceException;

    void saveData(Object data) throws ServiceException;

    void updateData(Integer primaryKey,String col1,boolean col2) throws ServiceException;

    void partialUpdateData(Integer primaryKey,String col1,Boolean col2) throws ServiceException;

    void deleteData(Integer primaryKey) throws ServiceException;
}
