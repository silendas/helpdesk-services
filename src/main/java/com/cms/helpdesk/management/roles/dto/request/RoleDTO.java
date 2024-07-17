package com.cms.helpdesk.management.roles.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RoleDTO {

    @NotBlank(message = "name Role should not be empty")
    private String name;

}
