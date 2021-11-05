package Planet.dao;

import Planet.entity.PlanetEntity;

public interface DAO {
    String save(PlanetEntity entity);
    PlanetEntity getById(int id);
    String update(PlanetEntity entity);
    String delete(int id);
}
