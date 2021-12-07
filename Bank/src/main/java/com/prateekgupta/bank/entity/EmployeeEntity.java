package com.prateekgupta.bank.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table
public class EmployeeEntity {
    @Id
    @GenericGenerator(name = "incrementer", strategy = "increment")
    @GeneratedValue(generator = "incrementer")
    private int id;
    private String name;
    private String designation;
    private float salary;
    private long contactNumber;

}
