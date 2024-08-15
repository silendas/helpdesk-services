package com.cms.helpdesk.tickets.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cms.helpdesk.enums.tickets.StatusEnum;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketListRes {

    private Long id;
    private String ticketNumber;
    private Date createdAt;
    private String createdBy;
    private OfficeRes office;
    private StatusEnum status;
    private String priority;
    private boolean isExternal;

}
