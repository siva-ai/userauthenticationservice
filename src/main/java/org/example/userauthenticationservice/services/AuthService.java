package org.example.userauthenticationservice.services;


import org.example.userauthenticationservice.exceptions.UserEmailAlreadyExistsException;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User findUserByEmail(String email,) {
        Optional<User> user=userRepo.findByEmail(email);
        if(user.isPresent()){
            throw new UserEmailAlreadyExistsException(" User with this email already exists");
        }
    }

    @Override
    public User saveUser(User user) {
        return null;
    }
}
