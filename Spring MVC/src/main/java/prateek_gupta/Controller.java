package prateek_gupta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    Service service;

    @RequestMapping(value = "save", method = RequestMethod.GET)
    ModelAndView save(){
        return new ModelAndView("save.html");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    ModelAndView save(String col_1,boolean col_2){
        ModelAndView modelAndView=new ModelAndView("result.jsp");
        modelAndView.addObject("message",service.save(col_1, col_2));
        return modelAndView;
    }
}
