package com.cms.helpdesk.attachments.dto;

import com.cms.helpdesk.tickets.model.Ticket;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttachmentDTO {
    @NotNull(message = "Ticket ID should not be empty")
    private Ticket ticketId;

    @NotNull(message = "Filetype should not be empty")
    private String filetype;

    @NotNull(message = "Filename should not be empty")
    private String filename;
}
