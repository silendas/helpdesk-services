package com.cms.helpdesk.tickets.dto;

import com.cms.helpdesk.enums.tickets.StatusEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTicketDTO {

    @NotBlank(message = "Ticket Number should not be empty")
    private String ticketNumber;

    @NotBlank(message = "Description should not be empty")
    private String description;

    private Long departmentId;
    private Long regionId;
    private Long branchId;

    @NotNull(message = "Constraint should not be empty")
    private Long constraintCategoryId;

    @NotNull(message = "Status should not be empty")
    private StatusEnum status;

    private boolean isExternal;
}
