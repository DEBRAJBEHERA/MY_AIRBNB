package com.airbnb.controller;

import com.airbnb.dto.LoginDto;
import com.airbnb.dto.PropertyUserDto;
import com.airbnb.dto.TokenResponse;
import com.airbnb.entity.PropertyUser;
import com.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
@Autowired
private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/addUser")
    public ResponseEntity<String>addUser(@RequestBody PropertyUserDto propertyUserDto){
        PropertyUser propertyUser = userService.addUser(propertyUserDto);
        if (propertyUser!=null){
            return new ResponseEntity<>("Registration Successful", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something went Wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @RequestMapping("/login")
    public ResponseEntity<?>login(@RequestBody LoginDto loginDto){
        String token= userService.verifyLogin(loginDto);
        if (token!=null){
            TokenResponse tokenResponse= new TokenResponse();
            tokenResponse.setToken(token);
            return new ResponseEntity<>(tokenResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>("invalid credential",HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/profile")
    public PropertyUser getCurrentUserProfile(@AuthenticationPrincipal PropertyUser user){
        return user;
    }
}
