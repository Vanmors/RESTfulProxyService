package org.example.DTO;

import lombok.Data;
import org.example.entity.Role;

@Data
public class UserDTO {

    private String username;
    private String password;
    private Role role;
}
