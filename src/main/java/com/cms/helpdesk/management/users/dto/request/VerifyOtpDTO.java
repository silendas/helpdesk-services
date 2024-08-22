package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOtpDTO {

    @NotBlank(message = "Otp tidak boleh kosong")
    @Size(min = 5, max = 5, message = "OTP harus 5 karakter")
    private String otp;

    @NotBlank(message = "Nip tidak boleh kosong")
    private String nip;

}
