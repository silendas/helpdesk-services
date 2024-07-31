package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOtpDto {

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Email tidak valid")
    private String email;
    
}
