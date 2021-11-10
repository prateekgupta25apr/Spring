package org.prateekgupta.dao;

import org.hibernate.SessionFactory;
import org.prateekgupta.entity.MobileEntity;
import org.springframework.beans.factory.annotation.Autowired;

public interface MobileDAO {
    String save(MobileEntity entity);
}
