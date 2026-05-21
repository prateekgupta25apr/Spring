package prateek_gupta.SampleProject.users.vo;

import lombok.Getter;
import lombok.Setter;
import prateek_gupta.SampleProject.users.entities.Users;

@Getter
@Setter
public class UsersVO {
    Integer userId;
    String firstName;
    String lastName;
    String email;
    byte[] password;
    boolean forgotPasswordRequest;
    boolean darkMode;

    public Users toEntity() {
        Users entity = new Users();
        entity.setUserId(userId);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setPassword(password);
        entity.setForgotPasswordRequest(forgotPasswordRequest);
        entity.setDarkMode(darkMode);
        return entity;
    }
}
