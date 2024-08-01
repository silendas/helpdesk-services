package com.cms.helpdesk.management.users.controller;

import org.springframework.web.bind.annotation.RestController;

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

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.users.dto.request.RegisterDto;
import com.cms.helpdesk.management.users.dto.request.ReqUserDTO;
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

    @GetMapping("/forgotpwd")
    public ResponseEntity<Object> forgotPassword(@RequestParam("email") String email, @RequestParam("nip") String nip) {
        String nipOrEmail;
        if (nip != null) {
            nipOrEmail = nip;
        } else {
            nipOrEmail = email;
        }
        return service.createLinkForgotPassword(nipOrEmail);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@Valid @RequestBody RegisterDto dto) {
        return service.register(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@Valid @RequestBody ReqUserDTO dto) {
        return service.createUser(dto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody ReqUserDTO dto) {
        return service.updateUser(id, dto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        return service.deleteUser(id);
    }

}
