package org.example.userauthenticationservice.dtos;


import lombok.Getter;
import lombok.Setter;
import org.example.userauthenticationservice.models.Role;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto
{
    private String email;

    private String password;
    private List<Role> roles = new ArrayList<>();
}
