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

    static Logger logger =Logger.getLogger(MobileController.class);

    @RequestMapping(value = "save",method = RequestMethod.GET)
    ModelAndView save(){
        return new ModelAndView("save.jsp");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    ModelAndView save(MobileDTO dto){
        logger.info(dto);
        ModelAndView modelAndView=new ModelAndView("result.jsp");
        String message=service.save(dto);
        logger.info(message);
        modelAndView.addObject("message",message);
        return modelAndView;
    }

    public static void main(String[] args) {

        logger.info("Hi");

        logger.trace("I am TRACE");
        logger.warn("I am WARN");
        logger.debug("I am DEBUG");
        logger.info("I am INFO");
        logger.fatal("I am FATAL");
        logger.error("I am ERROR");

    }
}
