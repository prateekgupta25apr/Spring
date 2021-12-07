package org.prateekgupta.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateAvailabilityByModelNameDTO {
    private String modelName;
    private String availability;
}
