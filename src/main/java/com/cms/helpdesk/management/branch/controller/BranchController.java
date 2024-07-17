package com.cms.helpdesk.management.branch.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.branch.dto.request.BranchDTO;
import com.cms.helpdesk.management.branch.service.BranchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = BasePath.BASE_BRANCHS)
public class BranchController {

    @Autowired
    private BranchService service;

    @GetMapping
    public ResponseEntity<Object> getBranchs(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getBranchs(page.orElse(0), size.orElse(10));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createBranch(@Valid @RequestBody BranchDTO dto) {
        return service.createBranch(dto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateBranch(@PathVariable("id") Long id, @RequestBody BranchDTO dto) {
        return service.updateBranch(id, dto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteBranch(@PathVariable("id") Long id) {
        return service.deleteBranch(id);
    }
}
