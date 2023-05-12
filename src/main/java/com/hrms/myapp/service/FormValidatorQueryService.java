package com.hrms.myapp.service;

import com.hrms.myapp.domain.*; // for static metamodels
import com.hrms.myapp.domain.FormValidator;
import com.hrms.myapp.repository.FormValidatorRepository;
import com.hrms.myapp.service.criteria.FormValidatorCriteria;
import com.hrms.myapp.service.dto.FormValidatorDTO;
import com.hrms.myapp.service.mapper.FormValidatorMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FormValidator} entities in the database.
 * The main input is a {@link FormValidatorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormValidatorDTO} or a {@link Page} of {@link FormValidatorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormValidatorQueryService extends QueryService<FormValidator> {

    private final Logger log = LoggerFactory.getLogger(FormValidatorQueryService.class);

    private final FormValidatorRepository formValidatorRepository;

    private final FormValidatorMapper formValidatorMapper;

    public FormValidatorQueryService(FormValidatorRepository formValidatorRepository, FormValidatorMapper formValidatorMapper) {
        this.formValidatorRepository = formValidatorRepository;
        this.formValidatorMapper = formValidatorMapper;
    }

    /**
     * Return a {@link List} of {@link FormValidatorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormValidatorDTO> findByCriteria(FormValidatorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FormValidator> specification = createSpecification(criteria);
        return formValidatorMapper.toDto(formValidatorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormValidatorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormValidatorDTO> findByCriteria(FormValidatorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FormValidator> specification = createSpecification(criteria);
        return formValidatorRepository.findAll(specification, page).map(formValidatorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormValidatorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FormValidator> specification = createSpecification(criteria);
        return formValidatorRepository.count(specification);
    }

    /**
     * Function to convert {@link FormValidatorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FormValidator> createSpecification(FormValidatorCriteria criteria) {
        Specification<FormValidator> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FormValidator_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), FormValidator_.type));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), FormValidator_.value));
            }
            if (criteria.getFormName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFormName(), FormValidator_.formName));
            }
            if (criteria.getFieldName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFieldName(), FormValidator_.fieldName));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompanyId(), FormValidator_.companyId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), FormValidator_.status));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), FormValidator_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), FormValidator_.lastModifiedBy));
            }
        }
        return specification;
    }
}
