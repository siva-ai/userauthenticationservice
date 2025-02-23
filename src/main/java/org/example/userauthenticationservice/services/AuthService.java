package org.example.userauthenticationservice.services;


import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.exceptions.UserEmailAlreadyExistsException;
import org.example.userauthenticationservice.exceptions.UserNotFoundException;
import org.example.userauthenticationservice.exceptions.UserPasswordMisMatchException;
import org.example.userauthenticationservice.models.Role;
import org.example.userauthenticationservice.models.Session;
import org.example.userauthenticationservice.models.Status;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repos.SessionRepo;
import org.example.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SessionRepo sessionRepo;

     @Autowired
     private SecretKey secretKey;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signUp(String email,String password) {
        Optional<User> userOptional=userRepo.findByEmail(email);
        if(userOptional.isPresent()){
            throw new UserEmailAlreadyExistsException(" User with this email already exists");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setCreatedAt(new Date());
        user.setLastModifiedAt(new Date());

        Role role = new Role();
        role.setName("CUSTOMER");

        List<Role> roles= new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

       return userRepo.save(user);

    }

    @Override
    public Pair<User,String> login(String email, String password) throws UserNotFoundException,UserPasswordMisMatchException{
        Optional<User> userOptional=userRepo.findByEmail(email);
        if(userOptional.isEmpty()){
           throw new UserNotFoundException("Email not registered");
        }
        User user = userOptional.get();
       // boolean b = bCryptPasswordEncoder.matches(password, user.getPassword());
        if( !bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new UserPasswordMisMatchException("password is incorrect");
        }

        Map<String,Object> payload = new HashMap<>();
        Long nowInMillies= System.currentTimeMillis();

        payload.put("iat",nowInMillies);
        payload.put("exp",nowInMillies+100000);
        payload.put("userId",userOptional.get().getId());
        payload.put("iss","scaler");
        payload.put("scope",userOptional.get().getRoles());

/*        String message="{\n" +
                "   \"email\": \"anurag@gmail.com\",\n" +
               "   \"roles\": [\n" +
               "      \"instructor\",\n" +
                "      \"buddy\"\n" +
                "   ],\n" +
                "   \"expirationDate\": \"2ndApril2025\"\n" +
               "}";*/

       // byte[] content= message.getBytes(StandardCharsets.UTF_8);
        String token=Jwts.builder().claims(payload).signWith(secretKey).compact();

        Session session=new Session();
        session.setToken(token);
        session.setUser(userOptional.get());
        session.setUser(user);
        session.setStatus(Status.ACTIVE);
        sessionRepo.save(session);



        MultiValueMap<String,Object> headers=new LinkedMultiValueMap<>();
        Pair<User,String> pair=new Pair(user,token);

        return pair;

    }


}
