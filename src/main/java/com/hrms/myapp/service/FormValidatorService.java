package com.hrms.myapp.service;

import com.hrms.myapp.domain.FormValidator;
import com.hrms.myapp.repository.FormValidatorRepository;
import com.hrms.myapp.service.dto.FormValidatorDTO;
import com.hrms.myapp.service.mapper.FormValidatorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FormValidator}.
 */
@Service
@Transactional
public class FormValidatorService {

    private final Logger log = LoggerFactory.getLogger(FormValidatorService.class);

    private final FormValidatorRepository formValidatorRepository;

    private final FormValidatorMapper formValidatorMapper;

    public FormValidatorService(FormValidatorRepository formValidatorRepository, FormValidatorMapper formValidatorMapper) {
        this.formValidatorRepository = formValidatorRepository;
        this.formValidatorMapper = formValidatorMapper;
    }

    /**
     * Save a formValidator.
     *
     * @param formValidatorDTO the entity to save.
     * @return the persisted entity.
     */
    public FormValidatorDTO save(FormValidatorDTO formValidatorDTO) {
        log.debug("Request to save FormValidator : {}", formValidatorDTO);
        FormValidator formValidator = formValidatorMapper.toEntity(formValidatorDTO);
        formValidator = formValidatorRepository.save(formValidator);
        return formValidatorMapper.toDto(formValidator);
    }

    /**
     * Update a formValidator.
     *
     * @param formValidatorDTO the entity to save.
     * @return the persisted entity.
     */
    public FormValidatorDTO update(FormValidatorDTO formValidatorDTO) {
        log.debug("Request to update FormValidator : {}", formValidatorDTO);
        FormValidator formValidator = formValidatorMapper.toEntity(formValidatorDTO);
        formValidator = formValidatorRepository.save(formValidator);
        return formValidatorMapper.toDto(formValidator);
    }

    /**
     * Partially update a formValidator.
     *
     * @param formValidatorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FormValidatorDTO> partialUpdate(FormValidatorDTO formValidatorDTO) {
        log.debug("Request to partially update FormValidator : {}", formValidatorDTO);

        return formValidatorRepository
            .findById(formValidatorDTO.getId())
            .map(existingFormValidator -> {
                formValidatorMapper.partialUpdate(existingFormValidator, formValidatorDTO);

                return existingFormValidator;
            })
            .map(formValidatorRepository::save)
            .map(formValidatorMapper::toDto);
    }

    /**
     * Get all the formValidators.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FormValidatorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormValidators");
        return formValidatorRepository.findAll(pageable).map(formValidatorMapper::toDto);
    }

    /**
     * Get one formValidator by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormValidatorDTO> findOne(Long id) {
        log.debug("Request to get FormValidator : {}", id);
        return formValidatorRepository.findById(id).map(formValidatorMapper::toDto);
    }

    /**
     * Delete the formValidator by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FormValidator : {}", id);
        formValidatorRepository.deleteById(id);
    }
}
