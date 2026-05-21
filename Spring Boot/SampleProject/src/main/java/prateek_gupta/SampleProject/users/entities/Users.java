package prateek_gupta.SampleProject.users.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import prateek_gupta.SampleProject.users.vo.UsersVO;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GenericGenerator(name = "autoincrement", strategy = "increment")
    @GeneratedValue(generator = "autoincrement")
    @Column(name = "user_id")
    Integer userId;

    @Column(name = "first_name", length = 100)
    String firstName;

    @Column(name = "last_name", length = 100)
    String lastName;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password")
    byte[] password;

    @Column(name = "forgot_password_request")
    Boolean forgotPasswordRequest;

    @Column(name = "dark_mode")
    Boolean darkMode;

    public UsersVO toVO() {
        UsersVO vo = new UsersVO();
        vo.setUserId(userId);
        vo.setFirstName(firstName != null ? firstName : "");
        vo.setLastName(lastName != null ? lastName : "");
        vo.setEmail(email);
        vo.setPassword(password);
        vo.setForgotPasswordRequest(
                forgotPasswordRequest != null ? forgotPasswordRequest : false);
        vo.setDarkMode(darkMode != null ? darkMode : false);
        return vo;
    }

    @Override
    public String toString() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}
