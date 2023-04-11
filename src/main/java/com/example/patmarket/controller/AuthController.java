package com.example.patmarket.controller;

import com.example.patmarket.entity.User;
import com.example.patmarket.model.Role;
import com.example.patmarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/reg")
    public String getRegistrationPage() {
        return "reg";
    }

    @PostMapping("/reg")
    public String addUserToDB(@RequestParam String email, @RequestParam String password) {
        if(!isEmailUsed(email)) {
            userRepository.save(new User(email,new BCryptPasswordEncoder().encode(password), Role.USER));
            return "login";
        }
        return "reg";
    }


    private boolean isEmailUsed(String email) {
        boolean check = false;
        try {
            if(userRepository.findByEmail(email).isPresent()) {
                check = true;
            }
        } catch (Exception exception) {
            //email not founded/ null pointer catch
        }
        return check;
    }
}
