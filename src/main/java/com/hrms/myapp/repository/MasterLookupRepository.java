package com.hrms.myapp.repository;

import com.hrms.myapp.domain.MasterLookup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MasterLookup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterLookupRepository extends JpaRepository<MasterLookup, Long>, JpaSpecificationExecutor<MasterLookup> {}
