package Monument.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
public class DTO {
    private String name;
    private String location;
    private short year;
    private int entryFee;
}
