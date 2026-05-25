package prateek_gupta.SampleProject.users.service;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

public interface UsersService {

    JSONObject login(
            JSONObject data, HttpServletResponse httpServletResponse) throws ServiceException;
    JSONObject signUp(
            JSONObject data, HttpServletResponse httpServletResponse) throws ServiceException;

    JSONObject forgotPassword(JSONObject data) throws ServiceException;

    JSONObject resetPassword(String pg, String password) throws ServiceException;

    JSONObject deleteUser() throws ServiceException;

    JSONObject changePassword(String password, String newPassword) throws ServiceException;

}
