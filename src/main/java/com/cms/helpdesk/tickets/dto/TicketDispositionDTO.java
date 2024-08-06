package com.cms.helpdesk.tickets.dto;

import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.tickets.model.Ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketDispositionDTO {

    @NotNull(message = "Ticket ID should not be empty")
    private Ticket ticketId;

    @NotBlank(message = "Description should not be empty")
    private String description;

    @NotNull(message = "User To should not be empty")
    private String userTo;
}
