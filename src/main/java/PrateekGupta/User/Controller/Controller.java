package PrateekGupta.User.Controller;

import PrateekGupta.User.DataTransferObject.DTO;
import PrateekGupta.User.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("service")
public class Controller {
    @Autowired
    Service service;

    @GetMapping("all")
    List<DTO> getAllUsers(){
        return service.getAllUsers();
    }

    @GetMapping("{id}")
    DTO getUser(@PathVariable int id){
        return service.getUser(id);
    }

    @PostMapping("add")
    String addUser(@RequestBody DTO dto){
        return service.addUser(dto);
    }

    @PutMapping("update")
    String updateUser(@RequestBody DTO dto){
        return service.updateUser(dto);
    }

    @DeleteMapping("delete/{id}")
    ResponseEntity<HttpStatus> deleteUser(int id){
        return service.deleteUser(id);
    }
}
