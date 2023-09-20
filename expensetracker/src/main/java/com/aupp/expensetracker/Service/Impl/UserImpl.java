package com.aupp.expensetracker.Service.Impl;

import com.aupp.expensetracker.DTO.UserDTO;
import com.aupp.expensetracker.DTO.LoginDTO;
import com.aupp.expensetracker.Service.UserService;
import com.aupp.expensetracker.entity.User;
import com.aupp.expensetracker.repository.UserRepo;
import com.aupp.expensetracker.response.LoginMesage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserDTO userDTO) {
        User user = new User(
                userDTO.getUserId(),
                userDTO.getUserName(),
                userDTO.getEmail(),
                this.passwordEncoder.encode(userDTO.getPassword())
        );
        userRepo.save(user);
        return user.getUserName();
    }

    UserDTO userDTO;

    @Override
    public LoginMesage loginUser(LoginDTO loginDTO) {
        String msg = "";
        User user1 = userRepo.findByEmail(loginDTO.getEmail());
        if (user1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = user1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<User> employee = userRepo.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (employee.isPresent()) {
                    return new LoginMesage("Login Success", true);
                } else {
                    return new LoginMesage("Login Failed", false);
                }
            } else {
                return new LoginMesage("password Not Match", false);
            }
        }else {
            return new LoginMesage("Email not exits", false);
        }
    }

    @Override
    public User findById(Integer id) {
        return userRepo.findById(id).get();
    }
    @Override
    public void delete(User user) {
        userRepo.delete(user);
    }
    @Override
    public void save(User user) {
        userRepo.save(user);
    }
    @Override
    public List < User > getAllUsersList() { return userRepo.findAll(); }
}

