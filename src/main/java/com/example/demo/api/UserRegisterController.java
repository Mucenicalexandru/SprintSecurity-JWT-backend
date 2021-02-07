package com.example.demo.api;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class UserRegisterController {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserRegisterController(){
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user) throws URISyntaxException {
        if(userRepository.existsByEmail(user.getEmail())){
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<>(responseHeaders, HttpStatus.CONFLICT);
        }else{
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
            User result = userRepository.save(user);
            return ResponseEntity.created(new URI("/api/register" + result.getId())).body(result);
        }
    }
}
