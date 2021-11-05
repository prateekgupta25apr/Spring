package Facebook.service;

import Facebook.dto.LoginDTO;
import Facebook.dto.UserDTO;
import Facebook.dto.LoginDTO;
import Facebook.dto.UserDTO;

public interface Service {
    String validateAndSave(UserDTO dto);
    UserDTO getById(int id);
    String validateAndLogin(LoginDTO dto);
    String updatePassword(String password,int id);
}
