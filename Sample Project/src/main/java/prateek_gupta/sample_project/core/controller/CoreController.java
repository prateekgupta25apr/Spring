package prateek_gupta.sample_project.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prateek_gupta.sample_project.core.service.CoreService;
import prateek_gupta.sample_project.core.vo.Table1VO;

@RestController
public class CoreController {
    @Autowired
    CoreService coreService;

    @GetMapping("get_table1_details")
    ResponseEntity<String> getTable1Details(@RequestParam Integer primaryKey){
        Table1VO table1VO=coreService.getTable1Details(primaryKey);
        return new ResponseEntity<>(table1VO.getCol1(), HttpStatus.OK);
    }
}
