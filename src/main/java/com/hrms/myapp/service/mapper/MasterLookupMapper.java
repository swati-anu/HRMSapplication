package com.hrms.myapp.service.mapper;

import com.hrms.myapp.domain.MasterLookup;
import com.hrms.myapp.service.dto.MasterLookupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MasterLookup} and its DTO {@link MasterLookupDTO}.
 */
@Mapper(componentModel = "spring")
public interface MasterLookupMapper extends EntityMapper<MasterLookupDTO, MasterLookup> {}
