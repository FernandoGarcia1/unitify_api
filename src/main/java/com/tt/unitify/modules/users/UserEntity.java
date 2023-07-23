package com.tt.unitify.modules.users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserEntity extends UserDto{
    String id;

}
