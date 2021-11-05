package Facebook.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String dob;
    private String gender;

    @Override
    public String toString() {
        return "User Details {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
