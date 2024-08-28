package com.cms.helpdesk.dashboard.dto.Separate;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketMontlyChart {

    private String month;

    private List<TicketStatusChart> tickets;
    
}
