package prateek_gupta.SampleProject.core;

import lombok.Getter;

import java.util.Map;

public class UserContext {

    /**
     * Field to store the user details in a Thread safe way
     */
    private static final ThreadLocal<UserContext> currentUser =
            new InheritableThreadLocal<>();

    public Integer userId;

    String jwt;
    String firstName;
    String lastName;
    String email;

    boolean darkMode;

    @Getter
    boolean isMobileAPI;

    double userLogoutTime;



    public UserContext(Map<String,Object> currentUser, String jwt) {
        this.userId = (Integer) currentUser.getOrDefault("user_id",0);
        this.firstName = (String) currentUser.getOrDefault("first_name", "");
        this.lastName = (String) currentUser.getOrDefault("last_name", "");
        this.email = (String) currentUser.getOrDefault("email", "");
        this.darkMode = (Boolean) currentUser.getOrDefault("dark_mode", false);
        this.isMobileAPI = (Boolean) currentUser.getOrDefault(
                "is_mobile_api", false);
        this.userLogoutTime = Double.parseDouble(String.valueOf(currentUser.getOrDefault(
                "user_logout_time", -1.00)));

        this.jwt = jwt;


    }

    public static void setCurrentUser(
            Map<String,Object> user, String jwt) {
        currentUser.set(new UserContext(user, jwt));
    }

    public static UserContext getCurrentUser() {
        return currentUser.get();
    }
}
