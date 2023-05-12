package com.hrms.myapp.web.rest;

import com.hrms.myapp.repository.FormValidatorRepository;
import com.hrms.myapp.service.FormValidatorQueryService;
import com.hrms.myapp.service.FormValidatorService;
import com.hrms.myapp.service.criteria.FormValidatorCriteria;
import com.hrms.myapp.service.dto.FormValidatorDTO;
import com.hrms.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hrms.myapp.domain.FormValidator}.
 */
@RestController
@RequestMapping("/api")
public class FormValidatorResource {

    private final Logger log = LoggerFactory.getLogger(FormValidatorResource.class);

    private static final String ENTITY_NAME = "formValidator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormValidatorService formValidatorService;

    private final FormValidatorRepository formValidatorRepository;

    private final FormValidatorQueryService formValidatorQueryService;

    public FormValidatorResource(
        FormValidatorService formValidatorService,
        FormValidatorRepository formValidatorRepository,
        FormValidatorQueryService formValidatorQueryService
    ) {
        this.formValidatorService = formValidatorService;
        this.formValidatorRepository = formValidatorRepository;
        this.formValidatorQueryService = formValidatorQueryService;
    }

    /**
     * {@code POST  /form-validators} : Create a new formValidator.
     *
     * @param formValidatorDTO the formValidatorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formValidatorDTO, or with status {@code 400 (Bad Request)} if the formValidator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-validators")
    public ResponseEntity<FormValidatorDTO> createFormValidator(@RequestBody FormValidatorDTO formValidatorDTO) throws URISyntaxException {
        log.debug("REST request to save FormValidator : {}", formValidatorDTO);
        if (formValidatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new formValidator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormValidatorDTO result = formValidatorService.save(formValidatorDTO);
        return ResponseEntity
            .created(new URI("/api/form-validators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /form-validators/:id} : Updates an existing formValidator.
     *
     * @param id the id of the formValidatorDTO to save.
     * @param formValidatorDTO the formValidatorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formValidatorDTO,
     * or with status {@code 400 (Bad Request)} if the formValidatorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formValidatorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/form-validators/{id}")
    public ResponseEntity<FormValidatorDTO> updateFormValidator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormValidatorDTO formValidatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormValidator : {}, {}", id, formValidatorDTO);
        if (formValidatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formValidatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formValidatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormValidatorDTO result = formValidatorService.update(formValidatorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formValidatorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /form-validators/:id} : Partial updates given fields of an existing formValidator, field will ignore if it is null
     *
     * @param id the id of the formValidatorDTO to save.
     * @param formValidatorDTO the formValidatorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formValidatorDTO,
     * or with status {@code 400 (Bad Request)} if the formValidatorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formValidatorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formValidatorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/form-validators/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormValidatorDTO> partialUpdateFormValidator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormValidatorDTO formValidatorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormValidator partially : {}, {}", id, formValidatorDTO);
        if (formValidatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formValidatorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formValidatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormValidatorDTO> result = formValidatorService.partialUpdate(formValidatorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formValidatorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /form-validators} : get all the formValidators.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formValidators in body.
     */
    @GetMapping("/form-validators")
    public ResponseEntity<List<FormValidatorDTO>> getAllFormValidators(
        FormValidatorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FormValidators by criteria: {}", criteria);
        Page<FormValidatorDTO> page = formValidatorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /form-validators/count} : count all the formValidators.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/form-validators/count")
    public ResponseEntity<Long> countFormValidators(FormValidatorCriteria criteria) {
        log.debug("REST request to count FormValidators by criteria: {}", criteria);
        return ResponseEntity.ok().body(formValidatorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /form-validators/:id} : get the "id" formValidator.
     *
     * @param id the id of the formValidatorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formValidatorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/form-validators/{id}")
    public ResponseEntity<FormValidatorDTO> getFormValidator(@PathVariable Long id) {
        log.debug("REST request to get FormValidator : {}", id);
        Optional<FormValidatorDTO> formValidatorDTO = formValidatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formValidatorDTO);
    }

    /**
     * {@code DELETE  /form-validators/:id} : delete the "id" formValidator.
     *
     * @param id the id of the formValidatorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/form-validators/{id}")
    public ResponseEntity<Void> deleteFormValidator(@PathVariable Long id) {
        log.debug("REST request to delete FormValidator : {}", id);
        formValidatorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
