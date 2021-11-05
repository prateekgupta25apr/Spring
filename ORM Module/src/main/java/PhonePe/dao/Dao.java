package PhonePe.dao;

import PhonePe.entity.LoginEntity;
import PhonePe.entity.UserEntity;

public interface Dao {
    String save(UserEntity entity);
    UserEntity getById(int id);
    LoginEntity getByAccountNumber(int accountNumber);
    String updatePassword(String password,int id);
}
