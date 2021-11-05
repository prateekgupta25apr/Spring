package PhonePe.entity;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_details")
@NamedQueries({
        @NamedQuery(name = "getById",query = "from UserEntity where id=:providedId")
})
public class UserEntity {
    @Id
    private int id;
    private String name;
    private int accountNUmber;
    private String password;
    private String ifscCode;
    private String address;
    
}
