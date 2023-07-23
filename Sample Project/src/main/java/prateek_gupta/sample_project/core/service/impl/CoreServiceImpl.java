package prateek_gupta.sample_project.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prateek_gupta.sample_project.core.dao.Table1Repository;
import prateek_gupta.sample_project.core.entities.Table1Entity;
import prateek_gupta.sample_project.core.service.CoreService;
import prateek_gupta.sample_project.core.vo.Table1VO;

@Service
public class CoreServiceImpl implements CoreService {

    @Autowired
    Table1Repository table1Repository;

    @Override
    public Table1VO getTable1Details(Integer primaryKey) {

        try{
            Table1Entity entity= table1Repository.findByPrimaryKey(primaryKey);
            return entity.toVO();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }
}
