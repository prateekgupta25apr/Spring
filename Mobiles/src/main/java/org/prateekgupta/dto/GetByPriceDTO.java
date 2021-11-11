package org.prateekgupta.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetByPriceDTO {
    private int minPrice;
    private int maxPrice;
}
