package Monument.controller;

import Monument.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    Service service;
    @RequestMapping(value = "save", method = RequestMethod.GET)
    String save(){
        System.out.println("Controller called");
        return service.save();
    }
}
