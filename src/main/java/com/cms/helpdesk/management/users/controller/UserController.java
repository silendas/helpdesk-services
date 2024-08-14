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
import com.cms.helpdesk.management.users.dto.request.ApprovalDto;
import com.cms.helpdesk.management.users.dto.request.ProfileUserDto;
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
            @RequestParam("search") Optional<String> search,
            @RequestParam("approval") Optional<String> approval,
            @RequestParam("departementId") Optional<Long> departementId,
            @RequestParam("roleId") Optional<Long> roleId,
            @RequestParam("pageable") Optional<Boolean> pageable,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getUsers(departementId.orElse(null), roleId.orElse(null), search.orElse(null), approval.orElse(""), pageable.orElse(false), page.orElse(0), size.orElse(10));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@Valid @RequestBody ReqUserDTO dto) {
        return service.createUser(dto);
    }

    @PatchMapping("/{nip}/update")
    public ResponseEntity<Object> updateUser(@PathVariable("nip") String nip, @RequestBody ReqUserDTO dto) {
        return service.updateUser(nip, dto);
    }

    @PatchMapping("/{nip}/approval")
    public ResponseEntity<Object> approveUser(@PathVariable("nip") String nip, @Valid @RequestBody ApprovalDto dto) {
        return service.approvalUser(nip, dto);
    }

    @PatchMapping("/{nip}/profile")
    public ResponseEntity<Object> profileUser(@PathVariable("nip") String nip, @RequestBody ProfileUserDto dto) {
        return service.profileEditUser(nip, dto);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/{nip}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable("nip") String nip) {
        return service.deleteUser(nip);
    }

}
