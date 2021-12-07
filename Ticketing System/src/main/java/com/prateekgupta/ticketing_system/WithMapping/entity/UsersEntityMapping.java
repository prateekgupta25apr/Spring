package com.prateekgupta.ticketing_system.WithMapping.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@ToString
public class UsersEntityMapping {
    @Id
    @GenericGenerator(name = "autoincrement",strategy = "increment")
    @GeneratedValue(generator = "autoincrement")
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
