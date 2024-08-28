package com.cms.helpdesk.dashboard.dto.Separate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketOfficeChart {

    private String name;

    private Long total;
    
}
