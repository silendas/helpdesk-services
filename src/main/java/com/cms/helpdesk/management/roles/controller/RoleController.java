package com.cms.helpdesk.management.roles.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.roles.dto.request.RoleDTO;
import com.cms.helpdesk.management.roles.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = BasePath.BASE_ROLES)
public class RoleController {

    @Autowired
    private RoleService service;

    @GetMapping
    public ResponseEntity<Object> getRoles(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getRoles(page.orElse(0), size.orElse(10));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createRole(@Valid @RequestBody RoleDTO dto) {
        return service.createRole(dto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateRole(@PathVariable("id") Long id, @Valid @RequestBody RoleDTO dto) {
        return service.updateRole(id, dto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteRole(@PathVariable("id") Long id) {
        return service.deleteRole(id);
    }
}
