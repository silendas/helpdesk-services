package com.cms.helpdesk.management.constraintcategory.dto;

import com.cms.helpdesk.enums.PriorityEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConstraintCategoryDTO {

    @NotBlank(message = "name Constraint should not be empty")
    private String name;

    @NotNull(message = "priority should not be empty")
    private PriorityEnum priority;

    @NotNull(message = "Department should not be empty")
    private Long departmentId;

    @NotNull(message = "Target Completion should not be empty")
    private Long targetCompletionId;
}
