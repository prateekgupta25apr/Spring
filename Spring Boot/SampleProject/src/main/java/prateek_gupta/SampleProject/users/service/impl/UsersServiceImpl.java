package prateek_gupta.SampleProject.users.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.core.SessionFilter;
import prateek_gupta.SampleProject.prateek_gupta.PasswordUtils;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.users.dao.UsersRepository;
import prateek_gupta.SampleProject.users.entities.Users;
import prateek_gupta.SampleProject.users.service.UsersService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class UsersServiceImpl implements UsersService {

    private final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    UsersRepository usersRepository;

    @Override
    public JSONObject login(
            JSONObject data, HttpServletResponse httpServletResponse) throws ServiceException {
        JSONObject response = new JSONObject();
        log.info("Entering Service : login()");
        try{
            String email=data.getString("email");
            String password=data.getString("password");
            Boolean rememberMe= data.has("remember_me") ?
                    data.getBoolean("remember_me"):null;

            Users user=usersRepository.findByEmail(email);

            boolean isPasswordValid= PasswordUtils.validPassword(password,user.getPassword());
            if (isPasswordValid){
                Map<String,Object> userDetails = prepareUserDetails(user,rememberMe);
                response.put("status",1);
                response.put("message","Login successful");
                response.put("user_details", userDetails);
                SessionFilter.updateSessionForLogin(userDetails,httpServletResponse);
            }
            else {
                response.put("status",2);
                response.put("message","Invalid password");
            }
        }catch(Exception e){
            throw new ServiceException(e.getMessage());
        }
        log.info("Exiting Service : login()");
        return response;
    }

    public Map<String, Object> prepareUserDetails(
            Users user){
        return prepareUserDetails(user,null,0);
    }

    public Map<String, Object> prepareUserDetails(
            Users user,double userLogoutTime){
        return prepareUserDetails(user,null,userLogoutTime);
    }

    public Map<String, Object> prepareUserDetails(
            Users user,Boolean rememberMe){
        return prepareUserDetails(user,rememberMe,0);
    }

    public Map<String, Object> prepareUserDetails(
            Users user,Boolean rememberMe,double userLogoutTime) {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("user_id", user.getUserId());
        userDetails.put("first_name", user.getFirstName());
        userDetails.put("last_name", user.getLastName());
        userDetails.put("email", user.getEmail());
        userDetails.put("dark_mode", user.getDarkMode());

        if (userLogoutTime > 0)
            userDetails.put("user_logout_time", userLogoutTime);

        if (rememberMe!=null){
            // Calculating logout time for the user and if the user has selected for
            // remember_me then we are setting user_logout_time to -1, which will be validated
            // in the middleware for user's logged in status
            if(rememberMe)
                userDetails.put("user_logout_time", -1.00);
            else{
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR,24);
                userDetails.put("user_logout_time",
                        Double.parseDouble(String.valueOf(calendar.getTimeInMillis())));
            }
        }
        return userDetails;
    }
}
