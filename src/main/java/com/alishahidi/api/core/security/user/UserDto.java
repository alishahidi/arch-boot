package com.alishahidi.api.core.security.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String name;
    String username;
    Set<Role> roles;
    Set<Permission> permissions;
    Date createdAt;
    Date updatedAt;

}
