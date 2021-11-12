package org.prateekgupta.dao;

import org.prateekgupta.dto.GetByBrandNameDTO;
import org.prateekgupta.dto.GetByPriceDTO;
import org.prateekgupta.dto.UpdateAvailabilityByModelNameDTO;
import org.prateekgupta.dto.UpdatePriceByModelNumberDTO;
import org.prateekgupta.entity.MobileEntity;

import java.util.List;

public interface MobileDAO {
    String save(MobileEntity entity);
    List<MobileEntity> getByPrice(GetByPriceDTO dto);
    List<MobileEntity> getByBrandName(GetByBrandNameDTO dto);
    MobileEntity updatePriceBuModelNumber(UpdatePriceByModelNumberDTO dto);
    MobileEntity updateAvailabilityBuModelName(UpdateAvailabilityByModelNameDTO dto);
}
