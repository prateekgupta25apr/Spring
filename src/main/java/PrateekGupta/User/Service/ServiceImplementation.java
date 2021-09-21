package PrateekGupta.User.Service;

import PrateekGupta.User.DataTransferObject.DTO;
import PrateekGupta.User.Entity.User;
import PrateekGupta.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceImplementation implements PrateekGupta.User.Service.Service {
    @Autowired
    UserRepository userRepository;

    @Override
    public List<DTO> getAllUsers() {
        List<DTO> dtoList=new ArrayList<>();
        List<User> usersList=userRepository.findAll();
        for (User user:usersList){
            DTO dto=new DTO();
            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setMobileNumber(user.getMobileNumber());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public DTO getUser(int id) {
        User user=userRepository.getOne(id);
        DTO dto=new DTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        return dto;
    }

    @Override
    public String addUser(DTO dto) {
        User user=new User();

        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setMobileNumber(dto.getMobileNumber());

        userRepository.save(user);

        return "User added successfully";
    }

    @Override
    public String updateUser(DTO dto) {
        User user=userRepository.getOne(dto.getId());

        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setMobileNumber(dto.getMobileNumber());

        userRepository.save(user);

        return "User updated successfully";
    }

    @Override
    public ResponseEntity<HttpStatus> deleteUser(int id) {
        try{
            userRepository.delete(userRepository.getOne(id));
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
