package Planet.service;

import Planet.dto.DTO;

public interface Service {
    String save(DTO dto);
    void getById(int id);
    String update(DTO dto);
    String delete(int id);
}
