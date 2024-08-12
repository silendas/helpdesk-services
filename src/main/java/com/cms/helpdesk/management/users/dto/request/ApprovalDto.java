package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovalDto {

    @NotNull(message = "Role tidak boleh kosong")
    private Long roleId;

    private Long departmentId = null;

    private Long regionId = null;
    
    private Long branchId = null;
    
}
