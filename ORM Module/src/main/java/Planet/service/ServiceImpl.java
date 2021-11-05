package Planet.service;

import Planet.dao.DAO;
import Planet.dao.DAOImpl;
import Planet.dto.DTO;
import Planet.entity.PlanetEntity;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceImpl implements Service{
    @Override
    public String save(DTO dto) {
        PlanetEntity entity=new PlanetEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        DAO dao=(DAO) new ClassPathXmlApplicationContext(
                "planet.xml").getBean("dao");
        return dao.save(entity);
    }

    @Override
    public void getById(int id) {
        DAO dao=(DAO) new ClassPathXmlApplicationContext(
                "planet.xml").getBean("dao");
        System.out.println(dao.getById(id));
    }

    @Override
    public String update(DTO dto) {
        PlanetEntity entity=new PlanetEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        DAO dao=(DAO) new ClassPathXmlApplicationContext(
                "planet.xml").getBean("dao");
        return dao.update(entity);
    }

    @Override
    public String delete(int id) {
        DAO dao=(DAO) new ClassPathXmlApplicationContext(
                "planet.xml").getBean("dao");
        return dao.delete(id);
    }
}
