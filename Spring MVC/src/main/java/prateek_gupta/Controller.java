package prateek_gupta;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class Controller {


    @RequestMapping(value = "save", method = RequestMethod.GET)
    ModelAndView save(){
        return new ModelAndView("save.html");
    }


}
