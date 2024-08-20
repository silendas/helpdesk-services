package com.cms.helpdesk.management.regions.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.management.regions.model.Region;

public class RegionFilter {

    public Specification<Region> byRegionId(Long regionId) {
        return (root, query, criteriaBuilder) -> {
            if (regionId == null)
                return null;
            return criteriaBuilder.equal(root.get("region").get("id"), regionId);
        };
    }
}
