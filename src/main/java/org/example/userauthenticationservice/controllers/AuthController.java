package org.example.userauthenticationservice.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.dtos.LoginRequestDto;
import org.example.userauthenticationservice.dtos.SignUpRequest;
import org.example.userauthenticationservice.dtos.UserDto;
import org.example.userauthenticationservice.dtos.ValidateTokenDto;
import org.example.userauthenticationservice.exceptions.UnAuthorizedException;
import org.example.userauthenticationservice.exceptions.UserEmailAlreadyExistsException;
import org.example.userauthenticationservice.exceptions.UserNotFoundException;
import org.example.userauthenticationservice.exceptions.UserPasswordMisMatchException;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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
          Pair<User,String> user=authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
          MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
          headers.add(HttpHeaders.SET_COOKIE,user.b);
          UserDto userDto= from(user.a);
          return new ResponseEntity<UserDto>(userDto,headers,HttpStatus.OK);
      }catch(UserNotFoundException e){
          return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }catch(UserPasswordMisMatchException e)
      {
          return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }
    }


    @GetMapping("/validateToken")
    public boolean validateToken(@RequestBody ValidateTokenDto validateTokenDto) throws UnAuthorizedException {
       boolean result= authService.validateToken(validateTokenDto.getToken(), validateTokenDto.getUserId());

       if(result==false)
       {
           throw new UnAuthorizedException("please Login again");
       }
return result;
    }



    private UserDto from(User user){
        UserDto userDto=new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
