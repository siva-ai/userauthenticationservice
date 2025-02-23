package org.example.userauthenticationservice.services;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.exceptions.UserEmailAlreadyExistsException;
import org.example.userauthenticationservice.exceptions.UserNotFoundException;
import org.example.userauthenticationservice.exceptions.UserPasswordMisMatchException;
import org.example.userauthenticationservice.models.User;

public interface IAuthService {


    public User signUp(String email,String password) throws UserEmailAlreadyExistsException;

    public Pair<User,String> login(String  email, String password) throws UserNotFoundException, UserPasswordMisMatchException;


}
