package Monument.service;

import Monument.dao.DAO;
import Monument.dto.DTO;
import Monument.entity.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceImpl implements Service {
    @Autowired
    DAO dao;
    @Override
    public String save(DTO dto) {
        Entity entity=new Entity();
        BeanUtils.copyProperties(dto,entity);
        return dao.save(entity);
    }
}
