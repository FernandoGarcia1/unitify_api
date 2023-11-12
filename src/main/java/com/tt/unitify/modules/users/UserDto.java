package com.tt.unitify.modules.users;

import lombok.Data;

@Data
public class UserDto {
    String name;
    String lastName;
    String email;
    String phone;
    String tokenFmc;
}
