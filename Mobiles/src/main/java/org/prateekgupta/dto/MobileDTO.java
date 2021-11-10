package org.prateekgupta.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MobileDTO {
    private String brandName;
    private int modelNumber;
    private String modelName;
    private String type;
    private int ram;
    private int rom;
    private int price;
    private String availability;
}
