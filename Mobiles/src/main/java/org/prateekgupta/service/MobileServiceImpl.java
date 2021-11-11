package org.prateekgupta.service;

import org.prateekgupta.dao.MobileDAO;
import org.prateekgupta.dto.MobileDTO;
import org.prateekgupta.entity.MobileEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MobileServiceImpl implements MobileService{

    @Autowired
    MobileDAO dao;

    @Override
    public String save(MobileDTO dto) {
        MobileEntity entity=new MobileEntity();
        BeanUtils.copyProperties(dto,entity);
        return dao.save(entity);
    }
}
