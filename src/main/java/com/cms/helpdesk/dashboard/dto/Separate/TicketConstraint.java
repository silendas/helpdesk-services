package com.cms.helpdesk.dashboard.dto.Separate;

import com.cms.helpdesk.enums.PriorityEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketConstraint {

    private String priority;

    private Long total;

    private Double percent;
    
}
