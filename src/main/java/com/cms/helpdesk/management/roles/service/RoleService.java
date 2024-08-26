package com.cms.helpdesk.management.roles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.common.reuse.PageConvert;

import com.cms.helpdesk.management.roles.dto.request.RoleDTO;
import com.cms.helpdesk.management.roles.filter.RoleFilter;
import com.cms.helpdesk.management.roles.model.Role;
import com.cms.helpdesk.management.roles.repository.PaginateRole;
import com.cms.helpdesk.management.roles.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PaginateRole paginate;

    public ResponseEntity<Object> getRoles(int page, int size) {
        Specification<Role> spec = Specification
                .where(new Filter<Role>().isNotDeleted())
                .and(new RoleFilter().notIncludeSuperadmin())
                .and(new Filter<Role>().orderByIdAsc());
        Page<Role> res = paginate.findAll(spec, PageRequest.of(page, size));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null), 1);
    }

    public ResponseEntity<Object> createRole(RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, roleRepository.save(role), null), 0);
    }

    public ResponseEntity<Object> updateRole(Long id, RoleDTO dto) {
        Role role = getRole(id);
        role.setName(dto.getName());
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, roleRepository.save(role), null), 0);
    }

    public ResponseEntity<Object> deleteRole(Long id) {
        Role role = getRole(id);
        role.setDeleted(true);
        roleRepository.save(role);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, null, null), 0);
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));
    }

}
