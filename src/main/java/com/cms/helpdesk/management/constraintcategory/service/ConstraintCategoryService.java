package com.cms.helpdesk.management.constraintcategory.service;

import java.lang.reflect.Field;
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
import com.cms.helpdesk.management.targetcompletion.model.TargetCompletion;
import com.cms.helpdesk.management.targetcompletion.repository.TargetCompletionRepository;

@Service
public class ConstraintCategoryService {

    @Autowired
    ConstraintCategoryRepository constraintRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    TargetCompletionRepository targetCompletionRepository;

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
        constraintCategory.setTargetCompletionId(getTargetCompletion(dto.getTargetCompletionId()));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, constraintRepository.save(constraintCategory),
                null), 0);
    }

    public ResponseEntity<Object> updateConstraint(Long id, ConstraintCategoryDTO dto) {
        ConstraintCategory constraint = getConstraint(id);

        ConstraintCategory request = new ConstraintCategory();

        request.setName(dto.getName());
        request.setPriority(dto.getPriority());
        request.setDepartmentId(getDepartment(dto.getDepartmentId()));
        request.setTargetCompletionId(getTargetCompletion(dto.getTargetCompletionId()));

        Field[] fields = request.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(request);
                if (value != null) {
                    Field constraintCatField = constraint.getClass().getDeclaredField(field.getName());
                    constraintCatField.setAccessible(true);
                    constraintCatField.set(constraint, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
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

    public TargetCompletion getTargetCompletion(Long id) {
        return targetCompletionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target Completion not found"));
    }
}
