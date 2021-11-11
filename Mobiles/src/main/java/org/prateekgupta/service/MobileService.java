package org.prateekgupta.service;

import org.prateekgupta.dto.GetByPriceDTO;
import org.prateekgupta.dto.MobileDTO;

import java.util.List;


public interface MobileService {
    String save(MobileDTO dto);
    List<MobileDTO> getByPrice(GetByPriceDTO dto);
}
