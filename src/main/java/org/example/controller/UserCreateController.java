package org.example.controller;

import org.example.DTO.UserDTO;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCreateController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public User createUser(@RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }


}
