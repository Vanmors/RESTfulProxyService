package org.example.service;

import org.example.DTO.UserDTO;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class  UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new UserDetailsImpl(user);
    }

    public User create(UserDTO userDTO) {
        User person = User.builder().
                username(userDTO.getUsername()).
                password(new BCryptPasswordEncoder().encode(userDTO.getPassword()) ).
                role(userDTO.getRole()).
                build();
        return userRepository.save(person);
    }
}
