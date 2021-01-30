package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserCredentials {

    private String username;
    private String password;


}
