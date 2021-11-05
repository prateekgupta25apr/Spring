package PhonePe.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginDTO {
    private int id;
    private int accountNUmber;
    private String password;
    private String confirmPassword;
}
