package com.cms.helpdesk.management.regions.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegionDTO {

    @NotBlank(message = "name Region should not be empty")
    private String name;
}
