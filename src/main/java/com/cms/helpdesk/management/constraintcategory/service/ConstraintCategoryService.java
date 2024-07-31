package com.cms.helpdesk.management.constraintcategory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.common.reuse.PageConvert;
import com.cms.helpdesk.management.constraintcategory.dto.ConstraintCategoryDTO;
import com.cms.helpdesk.management.constraintcategory.model.ConstraintCategory;
import com.cms.helpdesk.management.constraintcategory.repository.ConstraintCategoryRepository;
import com.cms.helpdesk.management.constraintcategory.repository.PaginateConstraintCategory;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.departments.repository.DepartmentRepository;

@Service
public class ConstraintCategoryService {

    @Autowired
    ConstraintCategoryRepository constraintRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    PaginateConstraintCategory paginate;

    public ResponseEntity<Object> getConstraints(int page, int size) {
        Specification<ConstraintCategory> spec = Specification
                .where(new Filter<ConstraintCategory>().isNotDeleted())
                .and(new Filter<ConstraintCategory>().orderByIdAsc());
        Page<ConstraintCategory> res = paginate.findAll(spec, PageRequest.of(page, size));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null), 1);
    }

    public ResponseEntity<Object> createConstraint(ConstraintCategoryDTO dto) {
        ConstraintCategory constraintCategory = new ConstraintCategory();
        constraintCategory.setName(dto.getName());
        constraintCategory.setPriority(dto.getPriority());
        constraintCategory.setDepartmentId(getDepartment(dto.getDepartmentId()));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, constraintRepository.save(constraintCategory),
                null), 0);
    }

    public ResponseEntity<Object> updateConstraint(Long id, ConstraintCategoryDTO dto) {
        ConstraintCategory constraint = getConstraint(id);

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            constraint.setName(dto.getName());
        }

        if (dto.getPriority() != null) {
            constraint.setPriority(dto.getPriority());
        }

        if (dto.getDepartmentId() != null && dto.getDepartmentId() != 0) {
            Department department = getDepartment(dto.getDepartmentId());
            constraint.setDepartmentId(department);
        }
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, constraintRepository.save(constraint), null), 0);

    }

    public ResponseEntity<Object> deleteConstraint(Long id) {
        ConstraintCategory constraintCategory = getConstraint(id);
        constraintCategory.setDeleted(true);
        constraintRepository.save(constraintCategory);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, null, null), 0);

    }

    public ConstraintCategory getConstraint(Long id) {
        return constraintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Constraint not found"));
    }

    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }
}
