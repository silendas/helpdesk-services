package com.cms.helpdesk.management.targetcompletion.service;

import java.lang.reflect.Field;

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
import com.cms.helpdesk.management.targetcompletion.dto.TargetCompletionDTO;
import com.cms.helpdesk.management.targetcompletion.model.TargetCompletion;
import com.cms.helpdesk.management.targetcompletion.repository.PaginateTargetCompletion;
import com.cms.helpdesk.management.targetcompletion.repository.TargetCompletionRepository;

@Service
public class TargetCompletionService {

    @Autowired
    private TargetCompletionRepository targetCompletionRepository;

    @Autowired
    private PaginateTargetCompletion paginate;

    public ResponseEntity<Object> getTargetCompletions(int page, int size) {
        Specification<TargetCompletion> spec = Specification
                .where(new Filter<TargetCompletion>().isNotDeleted())
                .and(new Filter<TargetCompletion>().orderByIdAsc());
        Page<TargetCompletion> res = paginate.findAll(spec, PageRequest.of(page, size));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null), 1);
    }

    public ResponseEntity<Object> createTargetCompletion(TargetCompletionDTO dto) {
        TargetCompletion targetCompletion = new TargetCompletion();
        targetCompletion.setName(dto.getName());
        targetCompletion.setValue(dto.getValue());
        targetCompletion.setTimeInterval(dto.getTimeInterval());
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, targetCompletionRepository.save(targetCompletion),
                null), 0);
    }

    public ResponseEntity<Object> updateTargetCompletion(Long id, TargetCompletionDTO dto) {
        TargetCompletion targetCompletion = gettTargetCompletion(id);
        TargetCompletion request = new TargetCompletion();

        request.setName(dto.getName());
        request.setValue(dto.getValue());
        request.setTimeInterval(dto.getTimeInterval());

        Field[] fields = request.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(request);
                if (value != null) {
                    Field targetCompletionField = targetCompletion.getClass().getDeclaredField(field.getName());
                    targetCompletionField.setAccessible(true);
                    targetCompletionField.set(targetCompletion, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, targetCompletionRepository.save(targetCompletion),
                null), 0);
    }

    public ResponseEntity<Object> deleteTargetCompletion(Long id) {
        TargetCompletion targetCompletion = gettTargetCompletion(id);
        targetCompletion.setDeleted(true);
        targetCompletionRepository.save(targetCompletion);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, null, null), 0);
    }

    public TargetCompletion gettTargetCompletion(Long id) {
        return targetCompletionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target Completion Not Found"));
    }
}
