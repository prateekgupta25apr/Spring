package Monument.controller;

import Monument.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    Service service;
    @RequestMapping("/welcome")
    String printWelcome(){
        System.out.println("Controller called");
        return service.printWelcome();
    }
}
