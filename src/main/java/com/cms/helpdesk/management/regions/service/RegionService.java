package com.cms.helpdesk.management.regions.service;

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

import com.cms.helpdesk.management.regions.dto.request.RegionDTO;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.management.regions.repository.PaginateRegion;
import com.cms.helpdesk.management.regions.repository.RegionRepository;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private PaginateRegion paginate;

    public ResponseEntity<Object> getRegions(boolean pageable, int page, int size) {
        Specification<Region> spec = Specification
                .where(new Filter<Region>().isNotDeleted())
                .and(new Filter<Region>().orderByIdAsc());
        if (pageable) {
            Page<Region> res = paginate.findAll(spec, PageRequest.of(page, size));
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null), 1);
        } else {
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), null, regionRepository.findAll(spec), null), 1);
        }
    }

    public ResponseEntity<Object> createRegion(RegionDTO dto) {
        Region region = new Region();
        region.setName(dto.getName());
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, regionRepository.save(region), null), 0);
    }

    public ResponseEntity<Object> updateRegion(Long id, RegionDTO dto) {
        Region region = getRegion(id);
        region.setName(dto.getName());
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, regionRepository.save(region), null), 0);
    }

    public ResponseEntity<Object> deleteRegion(Long id) {
        Region region = getRegion(id);
        region.setDeleted(true);
        regionRepository.save(region);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, region, null), 0);
    }

    public Region getRegion(Long id) {
        return regionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Region Not Found"));
    }
}
