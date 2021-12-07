package com.prateekgupta.bank.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountHolderDTO {
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
