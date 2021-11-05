package Facebook.dao;

import Facebook.entity.LoginEntity;
import Facebook.entity.UserEntity;

public interface DAO {
    String save(UserEntity entity);
    UserEntity getById(int id);
    LoginEntity getByEmail(String email);
    String updatePassword(String password,int id);
}
