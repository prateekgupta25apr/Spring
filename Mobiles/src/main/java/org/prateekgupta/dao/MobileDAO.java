package org.prateekgupta.dao;

import org.hibernate.SessionFactory;
import org.prateekgupta.dto.GetByPriceDTO;
import org.prateekgupta.entity.MobileEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface MobileDAO {
    String save(MobileEntity entity);
    List<MobileEntity> getByPrice(GetByPriceDTO dto);
}
