package PhonePe.service;

import PhonePe.dao.Dao;
import PhonePe.dao.DaoImpl;
import PhonePe.dto.LoginDTO;
import PhonePe.dto.UserDTO;
import PhonePe.entity.LoginEntity;
import PhonePe.entity.UserEntity;

import java.util.Objects;

public class ServiceImpl implements PhonePe.service.Service {
    @Override
    public String validateAndSave(UserDTO dto) {
        UserEntity entity=new UserEntity();
        if (dto !=null){
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setAccountNUmber(dto.getAccountNUmber());
            entity.setPassword(dto.getPassword());
            entity.setIfscCode(dto.getIfscCode());
            entity.setAddress(dto.getAddress());
        }
        Dao dao=new DaoImpl();
        return dao.save(entity);
    }

    @Override
    public UserDTO getById(int id) {
        Dao dao = new DaoImpl();
        UserEntity entity = dao.getById(id);
        UserDTO dto=new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAccountNUmber(entity.getAccountNUmber());
        dto.setPassword(entity.getPassword());
        dto.setAddress(entity.getAddress());
        dto.setIfscCode(entity.getIfscCode());
        return dto;
    }

    @Override
    public String validateAndLogin(LoginDTO dto) {
        if (!Objects.isNull(dto)) {
            if (dto.getAccountNUmber()>0) {
                if (dto.getPassword().equals(dto.getConfirmPassword())) {
                    Dao dao = new DaoImpl();
                    LoginEntity entity = dao.getByAccountNumber(dto.getAccountNUmber());
                    if (entity.getPassword().equals(dto.getPassword())) {
                        return "Login successful";
                    } else
                        return "Login failed";
                } else return "Passwords mismatch";
            } else return "Invalid email";
        } else return "Invalid account number and password";
    }

    @Override
    public String updatePassword(String password, int id) {
        if (id > 0) {
            if (!(password.equals(""))) {
                Dao dao=new DaoImpl();
                return dao.updatePassword(password,id);
            }else return "Invalid password";
        }else return "Invalid id";
    }
}
