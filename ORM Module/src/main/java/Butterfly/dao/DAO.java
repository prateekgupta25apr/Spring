package Butterfly.dao;

import Butterfly.entity.ButterflyEntity;

public interface DAO {
    String save(ButterflyEntity entity);
    ButterflyEntity getById(int id);
}
