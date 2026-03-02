package com.money.manage.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDTO {

    private String email;
    private String password;
    private String token;
}
