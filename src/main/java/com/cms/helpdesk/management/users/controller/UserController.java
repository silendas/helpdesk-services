package com.cms.helpdesk.management.users.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.branch.dto.request.BranchDTO;
import com.cms.helpdesk.management.users.dto.ReqUserDTO;
import com.cms.helpdesk.management.users.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = BasePath.BASE_USERS)
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<Object> getUsers(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getUsers(page.orElse(0), size.orElse(10));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@Valid @RequestBody ReqUserDTO dto) {
        return service.createUser(dto);
    }

    // @ PostMapping("/create")
    // public ResponseEntity<Object> createBranch(@Valid @RequestBody BranchDTO dto)
    // {
    // return service.createBranch(dto);
    // }
}
