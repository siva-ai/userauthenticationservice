package org.example.userauthenticationservice.controllers;

import org.example.userauthenticationservice.dtos.LoginRequestDto;
import org.example.userauthenticationservice.dtos.SignUpRequest;
import org.example.userauthenticationservice.dtos.UserDto;
import org.example.userauthenticationservice.exceptions.UserEmailAlreadyExistsException;
import org.example.userauthenticationservice.exceptions.UserNotFoundException;
import org.example.userauthenticationservice.exceptions.UserPasswordMisMatchException;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpRequest signUpRequest) {
        //String email = signUpRequest.getEmail();
        try{
            User user=authService.signUp(signUpRequest.getEmail(), signUpRequest.getPassword());
            return new ResponseEntity<>(from(user),HttpStatus.CREATED);
        }
        catch(UserEmailAlreadyExistsException e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto){
      try {
          User user=authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
          return new ResponseEntity<>(from(user),HttpStatus.OK);
      }catch(UserNotFoundException e){
          return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }catch(UserPasswordMisMatchException e)
      {
          return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }
    }



    private UserDto from(User user){

        UserDto userDto=new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
