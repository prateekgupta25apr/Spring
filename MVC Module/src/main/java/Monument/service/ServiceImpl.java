package Monument.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceImpl implements Service{
    @Override
    public String printWelcome() {
        System.out.println("ServiceImpl called");
        return "welcome.jsp";
    }
}
