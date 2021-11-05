package PhonePe.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private int id;
    private String name;
    private int accountNUmber;
    private String password;
    private String confirmPassword;
    private String ifscCode;
    private String address;

    @Override
    public String toString() {
        return "User Details{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountNUmber=" + accountNUmber +
                ", ifscCode='" + ifscCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
