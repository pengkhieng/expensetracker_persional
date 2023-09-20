package com.aupp.expensetracker.Controller;

import com.aupp.expensetracker.DTO.UserDTO;
import com.aupp.expensetracker.DTO.LoginDTO;
import com.aupp.expensetracker.Service.UserService;
import com.aupp.expensetracker.entity.User;
import com.aupp.expensetracker.response.LoginMesage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<?> getUsers() {
        Map<String, Object> jsonResponseMap = new LinkedHashMap<>();
        List<User> listOfUsers = userService.getAllUsersList();
        List<UserDTO> listOfUserDto = new ArrayList<>();

        if (!listOfUsers.isEmpty()) {
            for (User user : listOfUsers) {
                listOfUserDto.add(modelMapper.map(user, UserDTO.class));
            }
            jsonResponseMap.put("status", 1);
            jsonResponseMap.put("data", listOfUserDto);
            jsonResponseMap.put("count",listOfUserDto.size());
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.OK);
        } else {
            jsonResponseMap.clear();
            jsonResponseMap.put("status", 0);
            jsonResponseMap.put("message", "Data is not found");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/save")
    public String saveUser(@RequestBody UserDTO userDTO) {
        String id = userService.registerUser(userDTO);
        return id;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        LoginMesage loginResponse = userService.loginUser(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        Map<String, Object> jsonResponseMap = new LinkedHashMap<>();
        try {
            User user = userService.findById(id);
            // Convert entity to DTO
            UserDTO userDto = modelMapper.map(user, UserDTO.class);
            jsonResponseMap.put("status", 1);
            jsonResponseMap.put("data", userDto);
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.OK);
        } catch (Exception ex) {
            jsonResponseMap.clear();
            jsonResponseMap.put("status", 0);
            jsonResponseMap.put("message", "Data is not found");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        Map<String, Object> jsonResponseMap = new LinkedHashMap<>();
        try {
            User user = userService.findById(id);
            userService.delete(user);
            jsonResponseMap.put("status", 1);
            jsonResponseMap.put("message", "Record is deleted successfully!");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.OK);
        } catch (Exception ex) {
            jsonResponseMap.clear();
            jsonResponseMap.put("status", 0);
            jsonResponseMap.put("message", "Data is not found");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDto) {
        Map<String, Object> jsonResponseMap = new LinkedHashMap<>();
        try {
            User user = userService.findById(id);
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            if(user.getUserName().isEmpty() || user.getEmail().isEmpty() && user.getPassword().isEmpty()){
                jsonResponseMap.put("status", 0);
                jsonResponseMap.put("message", "Data is incorrect");
                return new ResponseEntity<>(jsonResponseMap, HttpStatus.NOT_FOUND);
            }
            else {
                userService.save(user);
                jsonResponseMap.put("status", 1);
                jsonResponseMap.put("data", userService.findById(id));
                return new ResponseEntity<>(jsonResponseMap, HttpStatus.OK);
            }
        } catch (Exception ex) {
            jsonResponseMap.clear();
            jsonResponseMap.put("status", 0);
            jsonResponseMap.put("message", "Data is not found");
            return new ResponseEntity<>(jsonResponseMap, HttpStatus.NOT_FOUND);
        }
    }
}
