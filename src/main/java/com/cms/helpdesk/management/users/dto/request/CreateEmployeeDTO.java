package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEmployeeDTO {

    @NotBlank(message = "NIP Should Not be Empty")
    private String nip;

}
