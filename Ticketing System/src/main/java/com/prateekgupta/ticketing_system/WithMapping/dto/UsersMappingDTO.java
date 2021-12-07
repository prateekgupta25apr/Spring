package com.prateekgupta.ticketing_system.WithMapping.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsersMappingDTO {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private long contactNumber;
    private String address;
    private String city;
    private String state;
    private String country;
}
