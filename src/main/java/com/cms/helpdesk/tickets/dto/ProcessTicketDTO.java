package com.cms.helpdesk.tickets.dto;

import java.util.Date;

import com.cms.helpdesk.enums.tickets.StatusEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProcessTicketDTO {

    @NotNull(message = "Target should not be empty")
    private Date targetCompletion;

    @NotNull(message = "Status should not be empty")
    private StatusEnum status;
}
