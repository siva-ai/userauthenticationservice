package org.example.userauthenticationservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenDto {

    private String token;
    private Long userId;
}
