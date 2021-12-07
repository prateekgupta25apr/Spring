package com.prateekgupta.bank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    private float amount;
    private  long accountNumber;
}
