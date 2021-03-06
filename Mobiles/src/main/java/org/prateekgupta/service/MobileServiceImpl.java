package org.prateekgupta.service;

import org.prateekgupta.dao.MobileDAO;
import org.prateekgupta.dto.*;
import org.prateekgupta.entity.MobileEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<MobileDTO> getByPrice(GetByPriceDTO dto) {
        List<MobileDTO> mobileDTOList=new ArrayList<>();

        List<MobileEntity> mobileEntityList=dao.getByPrice(dto);
        for (MobileEntity mobileEntity : mobileEntityList) {
            MobileDTO mobileDTO=new MobileDTO();
            BeanUtils.copyProperties(mobileEntity, mobileDTO);
            mobileDTOList.add(mobileDTO);
        }
        return mobileDTOList;
    }

    @Override
    public List<MobileDTO> getByBrandName(GetByBrandNameDTO dto) {
        List<MobileDTO> mobileDTOList=new ArrayList<>();

        List<MobileEntity> mobileEntityList=dao.getByBrandName(dto);
        for (MobileEntity mobileEntity : mobileEntityList) {
            MobileDTO mobileDTO=new MobileDTO();
            BeanUtils.copyProperties(mobileEntity, mobileDTO);
            mobileDTOList.add(mobileDTO);
        }
        return mobileDTOList;
    }

    @Override
    public MobileDTO updatePriceByModelNumber(UpdatePriceByModelNumberDTO dto) {
        MobileDTO mobileDTO=new MobileDTO();
        BeanUtils.copyProperties(dao.updatePriceBuModelNumber(dto),mobileDTO);
        return mobileDTO;
    }

    @Override
    public MobileDTO updateAvailabilityByModelName(UpdateAvailabilityByModelNameDTO dto) {
        MobileDTO mobileDTO=new MobileDTO();
        BeanUtils.copyProperties(dao.updateAvailabilityBuModelName(dto),mobileDTO);
        return mobileDTO;
    }
}
