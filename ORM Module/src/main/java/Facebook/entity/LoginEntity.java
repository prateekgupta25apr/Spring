package Facebook.entity;

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
        @NamedQuery(name = "getByEmail",
                query = "from LoginEntity where email=:providedEmail"),
        @NamedQuery(name = "updatePassword",
                query = "update LoginEntity set password=:providedPassword " +
                        "where id=:providedId")
})
public class LoginEntity {

    @Id
    private int id;
    private String email;
    private String password;
}
