package com.hrms.myapp.service.mapper;

import com.hrms.myapp.domain.FormValidator;
import com.hrms.myapp.service.dto.FormValidatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormValidator} and its DTO {@link FormValidatorDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormValidatorMapper extends EntityMapper<FormValidatorDTO, FormValidator> {}
