package com.cms.helpdesk.management.regions.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.regions.dto.request.RegionDTO;
import com.cms.helpdesk.management.regions.service.RegionService;

import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = BasePath.BASE_REGIONS)
public class RegionController {

    @Autowired
    private RegionService service;

    @GetMapping
    public ResponseEntity<Object> getRegions(
            @RequestParam("pageable") Optional<Boolean> pageable,
            @RequestParam("regionId") Optional<Long> regionId,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getRegions(regionId.orElse(null), pageable.orElse(false), page.orElse(0), size.orElse(10));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createRegion(@Valid @RequestBody RegionDTO dto) {
        return service.createRegion(dto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateRegion(@PathVariable("id") Long id, @Valid @RequestBody RegionDTO dto) {
        return service.updateRegion(id, dto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteRegion(@PathVariable("id") Long id) {
        return service.deleteRegion(id);
    }
}
