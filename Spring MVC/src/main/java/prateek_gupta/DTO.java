package prateek_gupta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTO {
    String col_1;
    boolean col_2;

    @Override
    public String toString() {
        return "DTO{" +
                "col_1='" + col_1 + '\'' +
                ", col_2='" + col_2 + '\'' +
                '}';
    }
}
