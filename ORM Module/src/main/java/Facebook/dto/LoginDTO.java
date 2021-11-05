package Facebook.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginDTO {
    private int id;
    private String email;
    private String password;
    private String confirmPassword;
}
