package prateek_gupta.SampleProject.core.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;
import prateek_gupta.SampleProject.core.service.CoreService;
import prateek_gupta.SampleProject.core.vo.Table1VO;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CoreController {
    @Autowired
    CoreService coreService;

    @GetMapping("get_table1_details")
    ResponseEntity<ObjectNode> getTable1Details(@RequestParam Integer primaryKey) {
        ResponseEntity<ObjectNode> response;
        try {
            if (primaryKey!=null&&primaryKey>0) {
                Table1VO table1VO = coreService.getTable1Details(primaryKey);
                if (table1VO != null) {
                    JSONObject data = new JSONObject();
                    data.put("table1", table1VO);
                    response = Util.getSuccessResponse(
                            "Successfully fetched the data", data);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("save_table1_details")
    ResponseEntity<ObjectNode> saveTable1Details(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try {
            String data=request.getParameter("data");
            if (data!=null) {
                coreService.saveTable1Details(data);
                response = Util.getSuccessResponse("Successfully fetched the data");
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

}
