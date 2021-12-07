package com.prateekgupta.bank.entity;

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
public class AccountHolderEntity {
    @Id
    @GenericGenerator(name = "incrementer", strategy = "increment")
    @GeneratedValue(generator = "incrementer")
    private int id;
    private long accountNumber;
    private String accountHolderName;
    private long contactNumber;
    private String email;
    private String address;
    private String branchName;
    private String typeOfAccount;
    private float amount;
    private String status;
    private String nomineeName;
    private String gender;
    private String dob;
}
