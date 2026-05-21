package prateek_gupta.SampleProject.users.service;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

public interface UsersService {

    JSONObject login(
            JSONObject data, HttpServletResponse httpServletResponse) throws ServiceException;

}
