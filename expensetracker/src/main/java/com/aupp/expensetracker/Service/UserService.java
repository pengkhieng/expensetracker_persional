package com.aupp.expensetracker.Service;

import com.aupp.expensetracker.DTO.UserDTO;
import com.aupp.expensetracker.DTO.LoginDTO;
import com.aupp.expensetracker.entity.User;
import com.aupp.expensetracker.response.LoginMesage;

import java.util.List;


public interface UserService {
    String registerUser(UserDTO userDTO);
    LoginMesage loginUser(LoginDTO loginDTO);
    List < User > getAllUsersList();
    User findById(Integer id);
    void delete(User user);
    void save(User user);
}
