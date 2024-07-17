package com.cms.helpdesk.management.departments.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.departments.dto.request.DepartmentDTO;
import com.cms.helpdesk.management.departments.service.DepartmentService;

import jakarta.validation.Valid;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = BasePath.BASE_DEPARTMENTS)
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @GetMapping
    public ResponseEntity<Object> getDepartments(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getDepartments(page.orElse(0), size.orElse(10));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createDepartment(@Valid @RequestBody DepartmentDTO dto) {
        return service.createDepartment(dto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateDepartment(@PathVariable("id") Long id, @Valid @RequestBody DepartmentDTO dto) {
        return service.updateDepartment(id, dto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteDepartment(@PathVariable("id") Long id) {
        return service.deleteDepartment(id);
    }
}
