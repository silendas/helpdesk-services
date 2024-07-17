package com.cms.helpdesk.management.branch.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BranchDTO {

    @NotBlank(message = "name Branch should not be empty")
    private String name;

    @NotNull(message = "Region should not be empty")
    private Long regionId;
}
