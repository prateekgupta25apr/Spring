package Butterfly.service;

import Butterfly.dao.DAO;
import Butterfly.dto.DTO;
import Butterfly.entity.ButterflyEntity;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceImpl implements Service{
    @Override
    public String save(DTO dto) {
        ButterflyEntity entity=new ButterflyEntity();
        entity.setId(dto.getId());
        entity.setAge(dto.getAge());
        entity.setOrigin(dto.getOrigin());
        entity.setAntennaeSize(dto.getAntennaeSize());
        entity.setSpeciesName(dto.getSpeciesName());
        entity.setWingColor(dto.getWingColor());
        entity.setWingSize(dto.getWingSize());
        DAO dao=(DAO) new ClassPathXmlApplicationContext(
                "butterfly.xml").getBean("dao");

        return dao.save(entity);
    }

    @Override
    public void getById(int id) {
        DAO dao=(DAO) new ClassPathXmlApplicationContext(
                "butterfly.xml").getBean("dao");
        System.out.println(dao.getById(id));
    }
}
