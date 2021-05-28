package com.example.data_service.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

@Data
@Builder
public class UserDTO implements Serializable {
    private int id;
    private String username;
    private String password;
    private String role;
    private String is_login;
    @Tolerate
    public UserDTO(){}
}