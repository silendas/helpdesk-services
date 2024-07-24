package com.cms.helpdesk.management.targetcompletion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TargetCompletionDTO {

    @NotBlank(message = "name should not be empty")
    private String name;

    @NotBlank(message = "value should not be empty")
    private String value;
}
