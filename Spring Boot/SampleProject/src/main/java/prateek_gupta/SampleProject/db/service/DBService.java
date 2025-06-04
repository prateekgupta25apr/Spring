package prateek_gupta.SampleProject.db.service;

import prateek_gupta.SampleProject.db.vo.Table1VO;

public interface DBService {
    Table1VO getTable1Details(Integer primaryKey);

    void saveTable1Details(String data);
}
