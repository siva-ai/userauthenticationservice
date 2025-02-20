package org.example.userauthenticationservice.services;


import org.example.userauthenticationservice.exceptions.UserEmailAlreadyExistsException;
import org.example.userauthenticationservice.exceptions.UserNotFoundException;
import org.example.userauthenticationservice.exceptions.UserPasswordMisMatchException;
import org.example.userauthenticationservice.models.Role;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

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
    public User login(String email, String password) throws UserNotFoundException,UserPasswordMisMatchException{
        Optional<User> userOptional=userRepo.findByEmail(email);
        if(userOptional.isEmpty()){
           throw new UserNotFoundException("Email not registered");
        }
        User user = userOptional.get();
       // boolean b = bCryptPasswordEncoder.matches(password, user.getPassword());
        if( !bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new UserPasswordMisMatchException("password is incorrect");
        }

        return user;

    }


}
