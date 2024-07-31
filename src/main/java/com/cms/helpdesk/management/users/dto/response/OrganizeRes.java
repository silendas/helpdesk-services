package com.cms.helpdesk.management.users.dto.response;

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
public class OrganizeRes {

    private Department department;
    private Branch branch;
    private Region region;

}
