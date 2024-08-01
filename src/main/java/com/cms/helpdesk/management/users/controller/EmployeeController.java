package com.cms.helpdesk.management.users.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.users.dto.request.ReqEmployeeDTO;
import com.cms.helpdesk.management.users.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = BasePath.BASE_EMPLOYEE)
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public ResponseEntity<Object> getEmployees(
            @RequestParam("pageable") Optional<Boolean> pageable,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getEmployees(pageable.orElse(false), page.orElse(0), size.orElse(10));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmployeeById(
            @RequestParam("nip") String nip) {
        return service.getEmployeeById(nip);
    }

    @GetMapping("/validate")
    public ResponseEntity<Object> getEmployeeByNip(
            @RequestParam("nip") String nip) {
        return service.getEmployeeByNIP(nip);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody ReqEmployeeDTO dto) {
        return service.saveEmployee(dto);
    }

    @PatchMapping("/{nip}/update")
    public ResponseEntity<Object> updateUser(@PathVariable("nip") String nip, @RequestBody ReqEmployeeDTO dto) {
        return service.updateEmployee(dto, nip);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/{nip}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable("nip") String nip) {
        return service.deleteEmployee(nip);
    }

}
