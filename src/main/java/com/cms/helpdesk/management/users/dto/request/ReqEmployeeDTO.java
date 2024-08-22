package com.cms.helpdesk.management.users.dto.request;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoAutoStart
public class ReqEmployeeDTO {
    
    @NotBlank(message = "NIP tidak boleh kosong")
    private String nip;

    private String name;

    private String phone;

    private Long departmentId = null;

    private Long regionId = null;
    
    private Long branchId = null;

}
