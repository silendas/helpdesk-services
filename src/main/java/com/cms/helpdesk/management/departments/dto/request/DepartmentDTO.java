package com.cms.helpdesk.management.departments.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentDTO {

    @NotBlank(message = "name Department should not be empty")
    private String name;
}
