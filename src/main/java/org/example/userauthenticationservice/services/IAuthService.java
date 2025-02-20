package org.example.userauthenticationservice.services;

import org.example.userauthenticationservice.models.User;

public interface IUserService {


    public User findUserByEmail(String email);

    public User saveUser(User user);


}
