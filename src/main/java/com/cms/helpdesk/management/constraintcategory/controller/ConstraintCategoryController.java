package com.cms.helpdesk.management.constraintcategory.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.constraintcategory.dto.ConstraintCategoryDTO;
import com.cms.helpdesk.management.constraintcategory.service.ConstraintCategoryService;

import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = BasePath.BASE_CONSTRAINT_CATEGORY)

public class ConstraintCategoryController {

    @Autowired
    private ConstraintCategoryService service;

    @GetMapping
    public ResponseEntity<Object> getConstraints(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getConstraints(page.orElse(0), size.orElse(10));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createConstraint(@Valid @RequestBody ConstraintCategoryDTO dto) {
        return service.createConstraint(dto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateConstraint(@PathVariable("id") Long id,
            @RequestBody ConstraintCategoryDTO dto) {
        return service.updateConstraint(id, dto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteConstraint(@PathVariable("id") Long id) {
        return service.deleteConstraint(id);
    }

}
