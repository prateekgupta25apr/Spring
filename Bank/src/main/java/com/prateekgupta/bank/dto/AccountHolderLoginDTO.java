package com.prateekgupta.bank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountHolderLoginDTO {
    private long accountNumber;
    private String accountHolderName;
}
