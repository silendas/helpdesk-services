package com.cms.helpdesk.management.departments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.common.reuse.PageConvert;

import com.cms.helpdesk.management.departments.dto.request.DepartmentDTO;
import com.cms.helpdesk.management.departments.filter.DepartementFilter;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.departments.repository.DepartmentRepository;
import com.cms.helpdesk.management.departments.repository.PaginateDepartment;
import com.cms.helpdesk.management.users.model.User;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PaginateDepartment paginate;

    public ResponseEntity<Object> getDepartments(Long departementId, boolean pageable, int page, int size) {
        Specification<Department> spec = Specification
                .where(new Filter<Department>().isNotDeleted())
                .and(new Filter<Department>().orderByIdAsc())
                .and(new DepartementFilter().byId(departementId));
        if (pageable) {
            Page<Department> res = paginate.findAll(spec, PageRequest.of(page, size));
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null), 1);
        } else {
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), null, departmentRepository.findAll(spec), null), 1);
        }
    }

    public ResponseEntity<Object> createDepartment(DepartmentDTO dto) {
        Department department = new Department();
        department.setName(dto.getName());
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, departmentRepository.save(department), null), 0);
    }

    public ResponseEntity<Object> updateDepartment(Long id, DepartmentDTO dto) {
        Department department = getDepartment(id);
        department.setName(dto.getName());
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, departmentRepository.save(department), null), 0);
    }

    public ResponseEntity<Object> deleteDepartment(Long id) {
        Department department = getDepartment(id);
        department.setDeleted(true);
        departmentRepository.save(department);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, null, null), 0);
    }

    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department Not Found"));
    }

}
