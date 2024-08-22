package com.cms.helpdesk.tickets.dto;

import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.regions.model.Region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OfficeRes {

    private Department department;
    private Region region;
    private Branch branch;
}
