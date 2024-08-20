package com.example.DTO;

import com.example.Models.User;
import com.example.Utils.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    private String name;

    @NotBlank
    private String mobile;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public User to(){
        return User.builder()
                .name(this.name)
                .mobile(this.mobile)
                .email(this.email)
                .password(this.password)
                .authorities(Constants.User_Authority)
                .build();
    }
}
