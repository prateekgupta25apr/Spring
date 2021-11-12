package org.prateekgupta.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdatePriceByModelNumberDTO {
    private int modelNumber;
    private int price;
}
