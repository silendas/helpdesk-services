package com.cms.helpdesk.management.branch.service;

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
import com.cms.helpdesk.management.branch.dto.request.BranchDTO;
import com.cms.helpdesk.management.branch.filter.BranchFilter;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.branch.repository.BranchRepository;
import com.cms.helpdesk.management.branch.repository.PaginateBranch;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.management.regions.repository.RegionRepository;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private PaginateBranch paginate;

    @Autowired
    private RegionRepository regionRepository;

    public ResponseEntity<Object> getBranchs(Long regionId, boolean pageable, int page, int size) {
        Specification<Branch> spec = Specification
                .where(new Filter<Branch>().isNotDeleted())
                .and(new BranchFilter().byRegionId(regionId))
                .and(new Filter<Branch>().orderByIdAsc());
        if(pageable) {
        Page<Branch> res = paginate.findAll(spec, PageRequest.of(page, size));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null), 1);
        } else {
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), null, branchRepository.findAll(spec), null), 1);
        }
    }

    public ResponseEntity<Object> createBranch(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setName(dto.getName());
        branch.setRegion(getRegion(dto.getRegionId()));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, branchRepository.save(branch), null), 0);
    }

    public ResponseEntity<Object> updateBranch(Long id, BranchDTO dto) {
        Branch branch = getBranch(id);

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            branch.setName(dto.getName());
        }

        if (dto.getRegionId() != null && dto.getRegionId() != 0) {
            Region region = getRegion(dto.getRegionId());
            if (region == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new GlobalDto(HttpStatus.BAD_REQUEST.value(), null, "Invalid region ID", null, null,
                                null));
            }
            branch.setRegion(region);
        }
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, branchRepository.save(branch), null), 0);
    }

    public ResponseEntity<Object> deleteBranch(Long id) {
        Branch branch = getBranch(id);
        branch.setDeleted(true);
        branchRepository.save(branch);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, null, null), 0);
    }

    public Branch getBranch(Long id) {
        return branchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Branch Not Found"));
    }

    public Region getRegion(Long id) {
        return regionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Region Not Found"));
    }
}
