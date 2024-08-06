package com.cms.helpdesk.tickets.dto;

import com.cms.helpdesk.management.users.model.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDispositionRes {

    private String userFrom;
    private String userTo;
    private String description;
}
