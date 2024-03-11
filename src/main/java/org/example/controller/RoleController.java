package org.example.controller;

import org.example.entity.Role;
import org.example.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleRepository repository;

    @PostMapping()
    public Role createRole(@RequestBody Role role) {
        return repository.save(role);
    }
}

