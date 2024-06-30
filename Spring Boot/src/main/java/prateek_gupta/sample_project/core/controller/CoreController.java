package prateek_gupta.sample_project.core.controller;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prateek_gupta.sample_project.base.SampleProjectException;
import prateek_gupta.sample_project.utils.Util;
import prateek_gupta.sample_project.core.service.CoreService;
import prateek_gupta.sample_project.core.vo.Table1VO;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CoreController {
    @Autowired
    CoreService coreService;

    @GetMapping("get_table1_details")
    ResponseEntity<JSONObject> getTable1Details(@RequestParam Integer primaryKey) {
        JSONObject response;
        try {
            if (primaryKey!=null&&primaryKey>0) {
                Table1VO table1VO = coreService.getTable1Details(primaryKey);
                if (table1VO != null) {
                    JSONObject data = new JSONObject();
                    data.put("table1", table1VO);
                    response = Util.getResponse(true,
                            "Successfully fetched the data", data);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (SampleProjectException exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("save_table1_details")
    ResponseEntity<JSONObject> saveTable1Details(HttpServletRequest request) {
        JSONObject response;
        try {
            String data=request.getParameter("data");
            if (data!=null) {
                coreService.saveTable1Details(data);
                response = Util.getResponse(true,
                        "Successfully fetched the data", null);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (SampleProjectException exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
