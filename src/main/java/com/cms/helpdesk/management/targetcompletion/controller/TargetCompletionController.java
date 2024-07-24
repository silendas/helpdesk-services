package com.cms.helpdesk.management.targetcompletion.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.targetcompletion.dto.TargetCompletionDTO;
import com.cms.helpdesk.management.targetcompletion.service.TargetCompletionService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = BasePath.BASE_TARGET_COMPLETION)
public class TargetCompletionController {

    @Autowired
    private TargetCompletionService service;

    @GetMapping
    public ResponseEntity<Object> getTargetCompletion(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getTargetCompletions(page.orElse(0), size.orElse(10));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createTargetCompletion(@Valid @RequestBody TargetCompletionDTO dto) {
        return service.createTargetCompletion(dto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateTargetCompletion(@PathVariable("id") Long id,
            @RequestBody TargetCompletionDTO dto) {
        return service.updateTargetCompletion(id, dto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteTargetCompletion(@PathVariable("id") Long id) {
        return service.deleteTargetCompletion(id);
    }
}
