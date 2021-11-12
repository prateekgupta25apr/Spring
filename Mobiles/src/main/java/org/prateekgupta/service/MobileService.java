package org.prateekgupta.service;

import org.prateekgupta.dto.*;

import java.util.List;


public interface MobileService {
    String save(MobileDTO dto);
    List<MobileDTO> getByPrice(GetByPriceDTO dto);
    List<MobileDTO> getByBrandName(GetByBrandNameDTO dto);
    MobileDTO updatePriceByModelNumber(UpdatePriceByModelNumberDTO dto);
    MobileDTO updateAvailabilityByModelName(UpdateAvailabilityByModelNameDTO dto);
}
