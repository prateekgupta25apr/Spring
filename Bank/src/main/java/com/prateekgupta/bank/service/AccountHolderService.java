package com.prateekgupta.bank.service;

import com.prateekgupta.bank.dto.AccountHolderDTO;
import com.prateekgupta.bank.dto.AccountHolderLoginDTO;
import com.prateekgupta.bank.dto.TransactionDTO;

public interface AccountHolderService {
    Object createAccount(AccountHolderDTO dto);
    Object getCurrentBalance(String accountHolderName);
    Object deposit(TransactionDTO dto);
    Object withdraw(TransactionDTO dto);
    Object closeAccount(AccountHolderLoginDTO dto);
    Object getAccountByBranchName(String branchName);
}
