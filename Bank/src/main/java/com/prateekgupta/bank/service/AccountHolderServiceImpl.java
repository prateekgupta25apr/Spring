package com.prateekgupta.bank.service;

import com.prateekgupta.bank.dto.AccountHolderDTO;
import com.prateekgupta.bank.dto.AccountHolderLoginDTO;
import com.prateekgupta.bank.dto.TransactionDTO;
import com.prateekgupta.bank.entity.AccountHolderEntity;
import com.prateekgupta.bank.entity.EmployeeEntity;
import com.prateekgupta.bank.repo.AccountHolderRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    @Autowired
    AccountHolderRepo repo;

    Logger logger = LoggerFactory.getLogger(AccountHolderServiceImpl.class);

    @Override
    public Object createAccount(AccountHolderDTO dto) {
        if (dto.getAccountNumber() == 0) {
            logger.warn("Account Number not found");
            return "Please enter valid Account Number";
        }

        if (dto.getAccountHolderName() == null) {
            logger.warn("Account Holder Name not found");
            return "Please enter valid Account Number";
        }

        if (dto.getContactNumber() == 0) {
            logger.warn("Contact Number not found");
            return "Please enter valid Contact Number";
        }

        if (dto.getEmail() == null) {
            logger.warn("Email not found");
            return "Please enter valid Email";
        }

        if (dto.getAddress() == null) {
            logger.warn("Address not found");
            return "Please enter valid Address";
        }

        if (dto.getBranchName() == null) {
            logger.warn("Branch Name not found");
            return "Please enter valid Branch Name";
        }

        if (dto.getTypeOfAccount() == null) {
            logger.warn("Type of Account not found");
            return "Please enter valid type of Account";
        }

        if (dto.getAmount() < 0) {
            logger.warn("Invalid Amount found");
            return "Please enter valid Amount";
        }

        if (dto.getStatus() == null) {
            logger.warn("Status not found");
            return "Please enter valid Status";
        }

        if (dto.getNomineeName() == null) {
            logger.warn("Nominee Name not found");
            return "Please enter valid Nominee Name";
        }

        if (dto.getGender() == null) {
            logger.warn("Gender not found");
            return "Please enter valid gender value";
        }

        if (dto.getDob() == null) {
            logger.warn("DOB not found");
            return "Please enter valid date of birth";
        }
        AccountHolderEntity entity = new AccountHolderEntity();
        try {
            BeanUtils.copyProperties(dto, entity);
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return "Invalid Account Details provided";
        }
        try {
            repo.save(entity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Invalid Account Details provided";
        }
        return "Account created";
    }

    @Override
    public Object getCurrentBalance(String accountHolderName) {
        AccountHolderDTO dto = new AccountHolderDTO();
        try {
            BeanUtils.copyProperties(repo.getByAccountHolderName(accountHolderName),
                    dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "No account associated with the provided Account Holder Name";
        }
        return dto.getAmount();
    }

    @Override
    public Object deposit(TransactionDTO dto) {
        if (dto.getAccountNumber() == 0) {
            logger.warn("Account Number not found");
            return "Please enter valid Account Number";
        }

        if (dto.getAmount() < 0) {
            logger.warn("Invalid Amount found");
            return "Please enter valid Amount";
        }

        AccountHolderEntity entity;
        try {
            entity = repo.getByAccountNumber(dto.getAccountNumber());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Invalid Account Number";
        }

        float totalAmount = dto.getAmount() + entity.getAmount();
        entity.setAmount(totalAmount);
        try {
            repo.save(entity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Transaction failed";
        }
        return "Amount updated";
    }

    @Override
    public Object withdraw(TransactionDTO dto) {
        if (dto.getAccountNumber() == 0) {
            logger.warn("Account Number not found");
            return "Please enter valid Account Number";
        }

        if (dto.getAmount() < 0) {
            logger.warn("Invalid Amount found");
            return "Please enter valid Amount";
        }

        AccountHolderEntity entity;
        try {
            entity = repo.getByAccountNumber(dto.getAccountNumber());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Invalid Account Number";
        }

        float totalAmount = entity.getAmount() - dto.getAmount();
        entity.setAmount(totalAmount);

        try {
            repo.save(entity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Transaction failed";
        }
        return "Amount updated";
    }

    @Override
    public Object closeAccount(AccountHolderLoginDTO dto) {
        if (dto.getAccountNumber() == 0) {
            logger.warn("Account Number not found");
            return "Please enter valid Account Number";
        }

        if (dto.getAccountHolderName() == null) {
            logger.warn("Account Holder Name not found");
            return "Please enter valid Account Number";
        }

        try {
            repo.delete(repo.getByAccountNumber(dto.getAccountNumber()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Account closing request failed";
        }
        return "Account deleted successfully";
    }

    @Override
    public Object getAccountByBranchName(String branchName) {
        AccountHolderDTO dto = new AccountHolderDTO();
        try{
            BeanUtils.copyProperties(repo.getByBranchName(branchName), dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "No accounts found for the provided branch name";
        }
        return dto;
    }
}
