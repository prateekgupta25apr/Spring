package Monument.service;

import Monument.dao.DAO;
import Monument.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceImpl implements Service {
    @Autowired
    DAO dao;
    @Override
    public String save() {
        Entity entity=new Entity();
        return dao.save(entity);
    }
}
