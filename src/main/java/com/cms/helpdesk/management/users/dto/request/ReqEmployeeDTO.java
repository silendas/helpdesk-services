package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReqEmployeeDTO {
    
    @NotBlank(message = "NIP Should Not be Empty")
    private String nip;

    @NotBlank(message = "Name Should Not be Empty")
    private String name;

    @NotBlank(message = "Phone Should Not be Empty")
    private String phone;

    private Long departmentId = null;

    private Long regionId = null;
    
    private Long branchId = null;

}
