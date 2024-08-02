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

    @NotBlank(message = "Name tidak boleh kosong")
    private String name;

    @NotBlank(message = "Phone tidak boleh kosong")
    private String phone;

    @NotNull(message = "Departement tidak boleh kosong")
    private Long departmentId = null;

    @NotNull(message = "Region tidak boleh kosong")
    private Long regionId = null;
    
    @NotNull(message = "Branch tidak boleh kosong")
    private Long branchId = null;

}
