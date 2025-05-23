package prateek_gupta.SampleProject.db.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import prateek_gupta.SampleProject.db.vo.Table1VO;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

public interface DBService {
    ObjectNode test(String testData) throws ServiceException;
    Table1VO getTable1Details(Integer primaryKey);

    void saveTable1Details(String data);
}
