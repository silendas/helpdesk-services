package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReqEmployeeDTO {
    
    @NotBlank(message = "NIP tidak boleh kosong")
    private String nip;

    private String name;

    private String phone;

    private Long departmentId = null;

    private Long regionId = null;
    
    private Long branchId = null;

}
