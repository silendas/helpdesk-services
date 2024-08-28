package com.cms.helpdesk.dashboard.dto.Separate;

import com.cms.helpdesk.enums.tickets.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusChart {

    private StatusEnum status;

    private Long total;
    
}
