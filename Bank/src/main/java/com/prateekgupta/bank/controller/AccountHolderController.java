package com.prateekgupta.bank.controller;

import com.prateekgupta.bank.dto.AccountHolderDTO;
import com.prateekgupta.bank.dto.AccountHolderLoginDTO;
import com.prateekgupta.bank.dto.TransactionDTO;
import com.prateekgupta.bank.service.AccountHolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountHolderController {

    private final Logger logger = LoggerFactory.getLogger(AccountHolderController.class);

    @Autowired
    AccountHolderService service;

    @PostMapping("create-account")
    Object createAccount(@RequestBody AccountHolderDTO dto) {
        if (dto == null) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.createAccount(dto);
    }

    @PostMapping("get-current-balance")
    Object getCurrentBalance(@RequestBody String accountHolderName) {
//        logger.trace("trace called");
//        logger.debug("debug called");
//        logger.info("info called");
        if (accountHolderName == null) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.getCurrentBalance(accountHolderName);
    }

    @PostMapping("deposit")
    Object deposit(@RequestBody TransactionDTO dto) {
        if (dto == null) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.deposit(dto);
    }

    @PostMapping("withdraw")
    Object withdraw(@RequestBody TransactionDTO dto) {
        if (dto == null) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.withdraw(dto);
    }

    @PostMapping("close-account")
    Object closeAccount(@RequestBody AccountHolderLoginDTO dto) {
        if (dto == null) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.closeAccount(dto);
    }

    @PostMapping("get-account-by-branch-name")
    Object getAccountByBranchName(@RequestBody String branchName) {
        if (branchName==null){
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.getAccountByBranchName(branchName);
    }
}
