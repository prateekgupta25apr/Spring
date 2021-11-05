package Butterfly.service;

import Butterfly.dto.DTO;

public interface Service {
    String save(DTO dto);
    void getById(int id);
}
