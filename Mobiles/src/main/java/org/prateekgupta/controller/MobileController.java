package org.prateekgupta.controller;

import org.apache.log4j.Logger;
import org.prateekgupta.dto.*;
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

    static Logger logger = Logger.getLogger(MobileController.class);

    @RequestMapping(value = "save", method = RequestMethod.GET)
    ModelAndView save() {
        return new ModelAndView("save.jsp");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    ModelAndView save(MobileDTO dto) {
        logger.info(dto);
        ModelAndView modelAndView = new ModelAndView("result.jsp");
        String message = service.save(dto);
        logger.info(message);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping(value = "get-by-price", method = RequestMethod.GET)
    ModelAndView getByPrice() {
        return new ModelAndView("getByPrice.jsp");
    }

    @RequestMapping(value = "get-by-price", method = RequestMethod.POST)
    ModelAndView getByPrice(GetByPriceDTO dto) {
        ModelAndView modelAndView = new ModelAndView("getByPrice.jsp");
        modelAndView.addObject("data", service.getByPrice(dto));
        return modelAndView;
    }

    @RequestMapping(value = "get-by-brand-name", method = RequestMethod.GET)
    ModelAndView getByBrandName() {
        return new ModelAndView("getByBrandName.jsp");
    }

    @RequestMapping(value = "get-by-brand-name", method = RequestMethod.POST)
    ModelAndView getByBrandName(GetByBrandNameDTO dto) {
        ModelAndView modelAndView = new ModelAndView("getByBrandName.jsp");
        modelAndView.addObject("data", service.getByBrandName(dto));
        return modelAndView;
    }

    @RequestMapping(value = "update-price-by-model-number", method = RequestMethod.GET)
    ModelAndView updatePriceByModelNumber() {
        return new ModelAndView("updatePriceByModelNumber.jsp");
    }

    @RequestMapping(value = "update-price-by-model-number", method = RequestMethod.POST)
    ModelAndView updatePriceByModelNumber(UpdatePriceByModelNumberDTO dto) {
        ModelAndView modelAndView = new ModelAndView("result.jsp");
        MobileDTO result = service.updatePriceByModelNumber(dto);
        String message = "Updated details are:" +
                " <br>BrandName:" + result.getBrandName() +
                " <br>ModelNumber: " + result.getModelNumber() +
                " <br>ModelName: " + result.getModelName() +
                " <br>Type: " + result.getType() +
                " <br>RAM: " + result.getRam() +
                " <br>ROM: " + result.getRom() +
                " <br>Price: " + result.getPrice() +
                " <br>Availability: " + result.getAvailability();
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping(value="update-availability-by-model-name",method = RequestMethod.GET)
    ModelAndView updateAvailabilityByModelName(){
        return new ModelAndView("updateAvailabilityByModelName.jsp");
    }

    @RequestMapping(value="update-availability-by-model-name",method = RequestMethod.POST)
    ModelAndView updateAvailabilityByModelName(UpdateAvailabilityByModelNameDTO dto){
        ModelAndView modelAndView=new ModelAndView("result.jsp");
        MobileDTO result= service.updateAvailabilityByModelName(dto);
        String message = "Updated details are:" +
                " <br>BrandName:" + result.getBrandName() +
                " <br>ModelNumber: " + result.getModelNumber() +
                " <br>ModelName: " + result.getModelName() +
                " <br>Type: " + result.getType() +
                " <br>RAM: " + result.getRam() +
                " <br>ROM: " + result.getRom() +
                " <br>Price: " + result.getPrice() +
                " <br>Availability: " + result.getAvailability();
        modelAndView.addObject("message", message);
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
