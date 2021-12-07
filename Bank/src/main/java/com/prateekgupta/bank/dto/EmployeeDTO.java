package com.prateekgupta.bank.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class EmployeeDTO {
    private String name;
    private String designation;
    private float salary;
    private long contactNumber;
}
