package com.prateekgupta.bank.repo;

import com.prateekgupta.bank.entity.AccountHolderEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface AccountHolderRepo extends
        JpaRepositoryImplementation<AccountHolderEntity,Integer> {
    AccountHolderEntity getByAccountNumber(long accountNumber);
    AccountHolderEntity getByAccountHolderName(String accountHolderName);
    AccountHolderEntity getByBranchName(String branchName);
}
