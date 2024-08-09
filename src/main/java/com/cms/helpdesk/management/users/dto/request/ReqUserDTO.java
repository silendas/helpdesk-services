package com.cms.helpdesk.management.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReqUserDTO {

    @NotBlank(message = "NIP tidak boleh kosong")
    private String nip;

    @NotBlank(message = "Email tidak boleh kosong")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    private String password;

    @NotNull(message = "Role tidak boleh kosong")
    private Long roleId;

    private boolean approval;
    private boolean active;

    @NotBlank(message = "Name tidak boleh kosong")
    private String name;

    @NotBlank(message = "Phone tidak boleh kosong")
    private String phone;

    private Long departmentId = null;

    private Long regionId = null;
    
    private Long branchId = null;

}
