package org.example.userauthenticationservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String password;

}
