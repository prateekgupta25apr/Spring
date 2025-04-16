package prateek_gupta.SampleProject.core.service;

import prateek_gupta.SampleProject.core.vo.Table1VO;

public interface CoreService {
    Table1VO getTable1Details(Integer primaryKey);

    void saveTable1Details(String data);
}
