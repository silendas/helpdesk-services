package com.cms.helpdesk.tickets.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTicketDTO {

    @NotBlank(message = "Description should not be empty")
    private String description;

    private Long departmentId;
    private Long regionId;
    private Long branchId;

    @NotNull(message = "Constraint should not be empty")
    private Long constraintCategoryId;

    private boolean isExternal;

    private List<MultipartFile> attachments;
}
