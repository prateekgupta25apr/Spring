package prateek_gupta.SampleProject.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.users.service.UsersService;

@RestController
@RequestMapping("db")
public class UsersController {

    private final Logger log = LoggerFactory.getLogger(UsersController.class);
    @Autowired
    UsersService usersService;


}
