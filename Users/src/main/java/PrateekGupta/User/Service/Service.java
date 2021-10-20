package PrateekGupta.User.Service;

import PrateekGupta.User.DataTransferObject.DTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface Service {
    List<DTO> getAllUsers();
    DTO getUser(int id);
    String addUser(DTO dto);
    String updateUser(DTO dto);
    ResponseEntity<HttpStatus> deleteUser(int id);
}
