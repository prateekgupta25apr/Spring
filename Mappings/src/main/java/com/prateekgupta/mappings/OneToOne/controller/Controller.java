package com.prateekgupta.mappings.OneToOne.controller;

import com.prateekgupta.mappings.OneToOne.entity.AddressDetails;
import com.prateekgupta.mappings.OneToOne.entity.BasicDetails;
import com.prateekgupta.mappings.OneToOne.repositories.AddressDetailsRepo;
import com.prateekgupta.mappings.OneToOne.repositories.BasicDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("test")
    Object test() {
        return "All good";
    }

}
