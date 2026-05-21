package prateek_gupta.SampleProject.core;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import prateek_gupta.SampleProject.prateek_gupta.Init;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.prateek_gupta.Utils;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is to filter out the requests and process cookies for the request to set
 * {@link UserContext}
 */
@Component
public class SessionFilter implements Filter {
    public static Map<Integer, String> userJWTMap = new ConcurrentHashMap<>();
    public static final String loggedOutKey="LOGGED_OUT";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=((HttpServletRequest) servletRequest);
        String cookie_name=Init.getConfiguration("cookie_name").toString();
        String cookie_secret=Init.getConfiguration("cookie_secret").toString();

        List<String> urlParts=Arrays.asList(request.getRequestURI().split("/"));
        List<String> noAuthPaths=Arrays.asList(
                "health_check",
                "well-known",
                "login",
                "sign_up",
                "forgot_password",
                "reset_password"
        );
        boolean noAuth=false;

        for (String noAuthPath : noAuthPaths) {
            if (urlParts.contains(noAuthPath)) {
                noAuth=true;
                break;
            }
        }

        // Extracting cookie
        String cookie="";
        if (request.getCookies()!=null){
            for (Cookie cookieObj : request.getCookies())
                // Validating the cookie name
                if (cookieObj.getName().equals(cookie_name))
                    cookie=cookieObj.getValue();
        }

        // decoding Cookies
        Map<String,Object> cookieData= Utils.processCookie(true,cookie_secret,cookie);


        // Setting context
        UserContext.setCurrentUser(cookieData, cookie);

        try {
            validateCookie(noAuth);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    static void validateCookie(boolean noOauth) throws ServiceException {
        UserContext userContext =
                UserContext.getCurrentUser()!=null?UserContext.getCurrentUser():null;

        if (userContext!=null){
            // This validates if the user is logged out based on time
            boolean timeBasedValidation = (
                    // Validating whether the logout time in jwt is greater than or not
                    userContext.userLogoutTime < System.currentTimeMillis()
                            &&
                    // Validating if user has asked to remember
                    userContext.userLogoutTime != -1
            );

            boolean jwtBasedValidation=(
                    // Validating if the provided jwt is in our record
                    SessionFilter.userJWTMap.containsKey(userContext.userId)
                    &&
                    // Once we log out the user, we store the value of self.logged_out_key
                    // against the userId in the dict user_jwt_map
                    SessionFilter.userJWTMap.get(userContext.userId).equals(
                            SessionFilter.loggedOutKey)
                    &&
                    // Validating is cached JWT and provided JWT are NOT same
                    // (for scenarios when user re-logins with old jwt)
                    !SessionFilter.userJWTMap.get(userContext.userId).equals(userContext.jwt)
                    );

            if (timeBasedValidation || jwtBasedValidation)
                SessionFilter.handleCookieException(noOauth);
        }

    }

    static void handleCookieException(
            boolean noOauth) throws ServiceException {
        String cookieValidation = Init.getConfiguration(
                        "cookie_validation").toString();

        if (StringUtils.isNotBlank(cookieValidation) &&
                cookieValidation.equalsIgnoreCase("true") && !noOauth)
            throw new ServiceException(ServiceException.ExceptionType.UNAUTHORIZED);

        else
            UserContext.setCurrentUser(new ConcurrentHashMap<>(),"");
    }

    public static void updateSessionForLogout(){
        SessionFilter.userJWTMap.put(
                UserContext.getCurrentUser().userId,  SessionFilter.loggedOutKey);
    }

    public static void updateSessionForLogin(
            Map<String, Object> cookieData,HttpServletResponse response){
        int userId=(Integer) cookieData.getOrDefault("user_id",0);
        if (userId>0){
            String cookie_name=Init.getConfiguration("cookie_name").toString();
            String cookie_secret=Init.getConfiguration("cookie_secret").toString();

            String jwtToken= Utils.processCookie(
                    false,cookie_secret,cookieData).toString();

            response.addCookie(new Cookie(cookie_name,jwtToken));
        }
    }
}
