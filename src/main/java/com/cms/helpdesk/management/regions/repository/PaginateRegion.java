package com.cms.helpdesk.management.regions.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.regions.model.Region;

@Repository
public interface PaginateRegion extends PagingAndSortingRepository<Region, Long>, JpaSpecificationExecutor<Region> {

}
