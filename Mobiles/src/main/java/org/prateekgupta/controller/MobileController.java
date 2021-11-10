package org.prateekgupta.controller;

import org.apache.log4j.Logger;
import org.prateekgupta.dto.MobileDTO;
import org.prateekgupta.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MobileController {
    @Autowired
    MobileService service;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    ModelAndView save(MobileDTO dto){
        ModelAndView modelAndView=new ModelAndView("result.jsp");
        modelAndView.addObject("message",service.save(dto));
        return modelAndView;
    }

    public static void main(String[] args) {
        Logger logger =Logger.getLogger(MobileController.class);
        logger.info("Hi");
    }
}
