package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileUserDto {

    @NotBlank(message = "Email tidak boleh kosong")
    private String email;

    private String password;

    @NotBlank(message = "Name tidak boleh kosong")
    private String name;

    @NotBlank(message = "Phone tidak boleh kosong")
    private String phone;
    
}
