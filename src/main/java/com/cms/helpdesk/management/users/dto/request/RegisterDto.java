package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {

    @NotBlank(message = "Nip tidak boleh kosong")
    private String nip;

    @NotBlank(message = "Name tidak boleh kosong")
    private String name;

    @NotBlank(message = "Phone tidak boleh kosong")
    private String phone;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Email tidak valid")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 8, message = "Password minimal 8 karakter")
    private String password;

}
