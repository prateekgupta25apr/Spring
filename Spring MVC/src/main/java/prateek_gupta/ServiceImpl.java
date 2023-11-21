package prateek_gupta;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceImpl implements Service {
    @Autowired
    DAO dao;
    @Override
    public String save(String col_1, boolean col_2) {
        Table1Entity entity=new Table1Entity();
        entity.col1= col_1;
        entity.col2= col_2;
        System.out.println(entity);
        return dao.save(entity);
    }
}
