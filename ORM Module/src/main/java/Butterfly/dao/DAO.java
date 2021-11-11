package Butterfly.dao;

import Butterfly.entity.ButterflyEntity;

public interface DAO {
    public String save(ButterflyEntity entity);
    public ButterflyEntity getById(int id);
}
