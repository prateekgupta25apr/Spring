package PhonePe.service;

import PhonePe.dto.LoginDTO;
import PhonePe.dto.UserDTO;

public interface Service {
    String validateAndSave(UserDTO dto);
    UserDTO getById(int id);
    String validateAndLogin(LoginDTO dto);
    String updatePassword(String password,int id);
}
