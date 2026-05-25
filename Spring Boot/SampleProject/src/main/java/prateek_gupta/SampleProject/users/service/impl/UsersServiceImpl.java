package prateek_gupta.SampleProject.users.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.core.SessionFilter;
import prateek_gupta.SampleProject.core.UserContext;
import prateek_gupta.SampleProject.multitenancy.TenantContext;
import prateek_gupta.SampleProject.prateek_gupta.Email;
import prateek_gupta.SampleProject.prateek_gupta.PasswordUtils;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.users.dao.UsersRepository;
import prateek_gupta.SampleProject.users.entities.Users;
import prateek_gupta.SampleProject.users.service.UsersService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersServiceImpl implements UsersService {

    private final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    UsersRepository usersRepository;

    @Autowired(required = false)
    Email emailService;

    @Override
    public JSONObject login(
            JSONObject data, HttpServletResponse httpServletResponse) throws ServiceException {
        JSONObject response = new JSONObject();
        log.info("Entering Service : login()");
        try {
            String email = data.getString("email");
            String password = data.getString("password");
            Boolean rememberMe = data.has("remember_me") ?
                    data.getBoolean("remember_me") : null;

            Users user = usersRepository.findByEmail(email);

            boolean isPasswordValid = PasswordUtils.validPassword(password, user.getPassword());
            if (isPasswordValid) {
                Map<String, Object> userDetails = prepareUserDetails(user, rememberMe);
                response.put("status", 1);
                response.put("message", "Login successful");
                response.put("user_details", userDetails);
                SessionFilter.updateSessionForLogin(userDetails, httpServletResponse);
            } else {
                response.put("status", 2);
                response.put("message", "Invalid password");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        log.info("Exiting Service : login()");
        return response;
    }

    @Override
    public JSONObject signUp(
            JSONObject data, HttpServletResponse httpServletResponse) throws ServiceException {
        JSONObject response = new JSONObject();
        log.info("Entering Service : signUp()");
        try {
            String firstName = data.getString("first_name");
            String lastName = data.getString("last_name");
            String email = data.getString("email");
            String password = data.getString("password");
            Boolean rememberMe = data.has("remember_me") ?
                    data.getBoolean("remember_me") : null;

            try {
                Users user = addUser(firstName, lastName, email, password);
                Map<String, Object> userDetails = prepareUserDetails(user, rememberMe);
                response.put("status", 1);
                response.put("message", "Sign up successful");
                response.put("user_details", userDetails);
                SessionFilter.updateSessionForLogin(userDetails, httpServletResponse);
            } catch (Exception e) {
                response.put("status", 2);
                response.put("message", "Provided email already exists");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        log.info("Exiting Service : signUp()");
        return response;
    }

    @Override
    public JSONObject forgotPassword(JSONObject data) throws ServiceException {
        JSONObject response = new JSONObject();
        log.info("Entering Service : forgotPassword()");
        try {
            String email = data.getString("email");
            boolean resendStatus = data.has("resend") && data.getBoolean("resend");

            Users user = usersRepository.findByEmail(email);
            if (user == null) {
                response.put("status", 2);
                response.put("email", email);
                response.put("message", "No account found for the provided email");
                log.info("Exiting Service : forgotPassword()");
                return response;
            }

            if (user.isForgotPasswordRequest() && !resendStatus) {
                response.put("status", 3);
                response.put("email", email);
                response.put("message",
                        "An email with link to reset the password is sent to your email.");
                log.info("Exiting Service : forgotPassword()");
                return response;
            }

            // Preparing the code
            // First we prepare the JSON object by setting the attribute "id" to user_id
            // in String format. Then we use the encode() method to convert the JSON object
            // in String format to bytes format, and then we store the JSON object in byte
            // format to a list, so we can see the JSON object as an array of numbers then
            // we use the map() method to convert the numbers stored in the List to String
            // format and then the join() method to combine all the numbers in the String
            // format to a single String.
            String forgotPasswordJson = "{\"id\":" + user.getUserId() + "}";
            StringBuilder codeBuilder = new StringBuilder();
            for (byte b : forgotPasswordJson.getBytes(StandardCharsets.UTF_8)) {
                codeBuilder.append(b);
            }
            String code = codeBuilder.toString();

            user.setForgotPasswordRequest(true);
            usersRepository.save(user);

            ServiceException.moduleLockCheck("EMAILS_ENABLED", true);

            String resetPasswordUrl =
                    TenantContext.getCurrentTenant().getApiUrl() + "reset_password";
            String emailSubject = "Reset Password link";
            String emailContent =
                    "<!DOCTYPE html><html><body style=\"font-family:Arial,sans-serif;"
                    + "color:#333;line-height:1.6;\"><p>Hello</p><p>We received a request "
                    + "to reset the password for your account.</p><p>If you made this "
                    + "request, please click the button below to choose a new password:"
                    + "</p><div><form action=\"" + resetPasswordUrl
                    + "\" method=\"GET\" target=\"_blank\" "
                    + "style=\"display:inline-block;\"><input type=\"hidden\" name=\"pg\" value=\""
                    + code + "\"><input type=\"submit\" value=\"Reset Password\" "
                    + "style=\"background-color:#007BFF;"
                    + "color:white;padding:12px 24px;border:none;border-radius:4px;"
                    + "font-weight:bold;cursor:pointer;\"></form></div><p>If you did "
                    + "not request a password reset, please ignore this email. Your "
                    + "account will remain secure.</p><p>Regards<br>Prateek Gupta</p>"
                    + "</body></html>";

            String fromEmail = prateek_gupta.SampleProject.prateek_gupta.Init.getConfiguration(
                    "EMAILS_DEFAULT_EMAIL",
                    "Prateek Gupta<prateek.gupta25apr@gmail.com>").toString();

            emailService.send(fromEmail, email, emailSubject, emailContent, null);

            response.put("status", 1);
            response.put("email", email);
            response.put("message",
                    "Email with link to reset password is sent to your email");
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException();
        }
        log.info("Exiting Service : forgotPassword()");
        return response;
    }

    @Override
    public JSONObject resetPassword(String pg, String password) throws ServiceException {
        JSONObject response = new JSONObject();
        log.info("Entering Service : resetPassword()");
        try {
            Users user;
            try {
                if (pg == null || pg.isBlank()) {
                    throw new ServiceException();
                }

                JSONObject pgDecoded = decoder(pg);

                if (pgDecoded == null || !pgDecoded.has("id")) {
                    throw new ServiceException();
                }

                int userId = pgDecoded.getInt("id");
                user = usersRepository.findByUserId(userId);

                if (user == null || !user.isForgotPasswordRequest()) {
                    throw new ServiceException();
                }
            } catch (Exception e) {
                response.put("status", 2);
                response.put("message", "Sorry, this page has expired");
                log.info("Exiting Service : resetPassword()");
                return response;
            }

            if (password == null || password.isBlank()) {
                throw new ServiceException();
            }

            user.setPassword(PasswordUtils.encryptPassword(password));
            user.setForgotPasswordRequest(false);
            usersRepository.save(user);

            response.put("status", 1);
            response.put("message", "Password reset successfully");
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException();
        }
        log.info("Exiting Service : resetPassword()");
        return response;
    }

    @Override
    public JSONObject deleteUser() throws ServiceException {
        JSONObject response = new JSONObject();
        log.info("Entering Service : deleteUser()");
        try {
            prateek_gupta.SampleProject.project_utils.Init.validateUserLogin();
            UserContext userContext = UserContext.getCurrentUser();
            String message = "";

            try {
                Users user = usersRepository.findByUserId(userContext.userId);
                if (user == null) {
                    throw new ServiceException();
                }
                usersRepository.delete(user);

                // Logging out the user
                SessionFilter.updateSessionForLogout();

                if (userContext.isMobileAPI()) {
                    response.put("status", 1);
                    response.put("message",
                            "Successfully deleted account with id " + user.getUserId());
                } else {
                    message = "Successfully deleted your account";
                }
            } catch (Exception e) {
                if (userContext.isMobileAPI()) {
                    response.put("status", 2);
                    response.put("message",
                            "An error occurred while deleting account with Id "
                                    + userContext.userId);
                } else {
                    message = "An error occurred while deleting your account";
                }
            }

            if (!userContext.isMobileAPI()) {
                response.put("message", message);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException();
        }
        log.info("Exiting Service : deleteUser()");
        return response;
    }

    private static JSONObject decoder(String code) {
        // Converting the code to a JSON Object
        List<Integer> rawJson = new ArrayList<>();
        String remaining = code;
        while (!remaining.isEmpty()) {
            String threeDigit = remaining.length() >= 3 ?
                    remaining.substring(0, 3) : remaining;
            if (Integer.parseInt(threeDigit) > 256) {
                String twoDigit = remaining.length() >= 2 ?
                        remaining.substring(0, 2) : remaining;
                rawJson.add(Integer.parseInt(twoDigit));
                remaining = remaining.substring(twoDigit.length());
            } else {
                rawJson.add(Integer.parseInt(threeDigit));
                remaining = remaining.substring(threeDigit.length());
            }
        }
        byte[] bytes = new byte[rawJson.size()];
        for (int i = 0; i < rawJson.size(); i++) {
            bytes[i] = rawJson.get(i).byteValue();
        }
        return JSONObject.fromObject(new String(bytes, StandardCharsets.UTF_8));
    }

    Users addUser(
            String firstName, String lastName, String email, String password)
            throws ServiceException {
        Users user = new Users();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(PasswordUtils.encryptPassword(password));
        user = usersRepository.save(user);
        return user;
    }

    public Map<String, Object> prepareUserDetails(
            Users user) {
        return prepareUserDetails(user, null, 0);
    }

    public Map<String, Object> prepareUserDetails(
            Users user, double userLogoutTime) {
        return prepareUserDetails(user, null, userLogoutTime);
    }

    public Map<String, Object> prepareUserDetails(
            Users user, Boolean rememberMe) {
        return prepareUserDetails(user, rememberMe, 0);
    }

    public Map<String, Object> prepareUserDetails(
            Users user, Boolean rememberMe, double userLogoutTime) {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("user_id", user.getUserId());
        userDetails.put("first_name", user.getFirstName());
        userDetails.put("last_name", user.getLastName());
        userDetails.put("email", user.getEmail());
        userDetails.put("dark_mode", user.isDarkMode());

        if (userLogoutTime > 0)
            userDetails.put("user_logout_time", userLogoutTime);

        if (rememberMe != null) {
            // Calculating logout time for the user and if the user has selected for
            // remember_me then we are setting user_logout_time to -1, which will be validated
            // in the middleware for user's logged in status
            if (rememberMe)
                userDetails.put("user_logout_time", -1.00);
            else {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR, 24);
                userDetails.put("user_logout_time",
                        Double.parseDouble(String.valueOf(calendar.getTimeInMillis())));
            }
        }
        return userDetails;
    }
}
