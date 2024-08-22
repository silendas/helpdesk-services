package com.cms.helpdesk.tickets.dto;

import java.util.Date;

import com.cms.helpdesk.enums.tickets.StatusEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CloseTicketDTO {

    @NotNull(message = "Time Completion should not be empty")
    private Date timeCompletion;

    @NotBlank(message = "Description Completion shoul not be empty")
    private String descriptionCompletion;

    @NotNull(message = "Status should not be empty")
    private StatusEnum status;

}
