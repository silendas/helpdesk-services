package com.cms.helpdesk.management.targetcompletion.dto;

import com.cms.helpdesk.enums.targetcompletion.TimeIntervalEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TargetCompletionDTO {

    @NotBlank(message = "name should not be empty")
    private String name;

    @NotNull(message = "value should not be empty")
    private Integer value;

    @NotNull(message = "time interval should not be empty")
    private TimeIntervalEnum timeInterval;
}
