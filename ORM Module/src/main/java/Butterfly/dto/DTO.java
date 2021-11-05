package Butterfly.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DTO {
    private int id;
    private String speciesName;
    private int wingSize;
    private String wingColor;
    private int age;
    private String origin;
    private int antennaeSize;
}
