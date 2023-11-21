package prateek_gupta;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceImpl implements Service {
    @Autowired
    DAO dao;
    @Override
    public String save(DTO dto) {
        Table1Entity entity=new Table1Entity();
        BeanUtils.copyProperties(dto,entity);
        return dao.save(entity);
    }
}
