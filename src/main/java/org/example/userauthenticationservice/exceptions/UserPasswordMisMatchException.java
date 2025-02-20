package org.example.userauthenticationservice.exceptions;

public class UserPasswordMisMatchException extends Exception {
    public UserPasswordMisMatchException(String message){
        super(message);
    }
}
