package com.hrms.myapp.web.rest;

import com.hrms.myapp.repository.MasterLookupRepository;
import com.hrms.myapp.service.MasterLookupQueryService;
import com.hrms.myapp.service.MasterLookupService;
import com.hrms.myapp.service.criteria.MasterLookupCriteria;
import com.hrms.myapp.service.dto.MasterLookupDTO;
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
 * REST controller for managing {@link com.hrms.myapp.domain.MasterLookup}.
 */
@RestController
@RequestMapping("/api")
public class MasterLookupResource {

    private final Logger log = LoggerFactory.getLogger(MasterLookupResource.class);

    private static final String ENTITY_NAME = "masterLookup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MasterLookupService masterLookupService;

    private final MasterLookupRepository masterLookupRepository;

    private final MasterLookupQueryService masterLookupQueryService;

    public MasterLookupResource(
        MasterLookupService masterLookupService,
        MasterLookupRepository masterLookupRepository,
        MasterLookupQueryService masterLookupQueryService
    ) {
        this.masterLookupService = masterLookupService;
        this.masterLookupRepository = masterLookupRepository;
        this.masterLookupQueryService = masterLookupQueryService;
    }

    /**
     * {@code POST  /master-lookups} : Create a new masterLookup.
     *
     * @param masterLookupDTO the masterLookupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new masterLookupDTO, or with status {@code 400 (Bad Request)} if the masterLookup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/master-lookups")
    public ResponseEntity<MasterLookupDTO> createMasterLookup(@RequestBody MasterLookupDTO masterLookupDTO) throws URISyntaxException {
        log.debug("REST request to save MasterLookup : {}", masterLookupDTO);
        if (masterLookupDTO.getId() != null) {
            throw new BadRequestAlertException("A new masterLookup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MasterLookupDTO result = masterLookupService.save(masterLookupDTO);
        return ResponseEntity
            .created(new URI("/api/master-lookups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /master-lookups/:id} : Updates an existing masterLookup.
     *
     * @param id the id of the masterLookupDTO to save.
     * @param masterLookupDTO the masterLookupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterLookupDTO,
     * or with status {@code 400 (Bad Request)} if the masterLookupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the masterLookupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/master-lookups/{id}")
    public ResponseEntity<MasterLookupDTO> updateMasterLookup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MasterLookupDTO masterLookupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MasterLookup : {}, {}", id, masterLookupDTO);
        if (masterLookupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterLookupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterLookupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MasterLookupDTO result = masterLookupService.update(masterLookupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterLookupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /master-lookups/:id} : Partial updates given fields of an existing masterLookup, field will ignore if it is null
     *
     * @param id the id of the masterLookupDTO to save.
     * @param masterLookupDTO the masterLookupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterLookupDTO,
     * or with status {@code 400 (Bad Request)} if the masterLookupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the masterLookupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the masterLookupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/master-lookups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MasterLookupDTO> partialUpdateMasterLookup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MasterLookupDTO masterLookupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MasterLookup partially : {}, {}", id, masterLookupDTO);
        if (masterLookupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterLookupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterLookupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MasterLookupDTO> result = masterLookupService.partialUpdate(masterLookupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterLookupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /master-lookups} : get all the masterLookups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of masterLookups in body.
     */
    @GetMapping("/master-lookups")
    public ResponseEntity<List<MasterLookupDTO>> getAllMasterLookups(
        MasterLookupCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MasterLookups by criteria: {}", criteria);
        Page<MasterLookupDTO> page = masterLookupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /master-lookups/count} : count all the masterLookups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/master-lookups/count")
    public ResponseEntity<Long> countMasterLookups(MasterLookupCriteria criteria) {
        log.debug("REST request to count MasterLookups by criteria: {}", criteria);
        return ResponseEntity.ok().body(masterLookupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /master-lookups/:id} : get the "id" masterLookup.
     *
     * @param id the id of the masterLookupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the masterLookupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/master-lookups/{id}")
    public ResponseEntity<MasterLookupDTO> getMasterLookup(@PathVariable Long id) {
        log.debug("REST request to get MasterLookup : {}", id);
        Optional<MasterLookupDTO> masterLookupDTO = masterLookupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(masterLookupDTO);
    }

    /**
     * {@code DELETE  /master-lookups/:id} : delete the "id" masterLookup.
     *
     * @param id the id of the masterLookupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/master-lookups/{id}")
    public ResponseEntity<Void> deleteMasterLookup(@PathVariable Long id) {
        log.debug("REST request to delete MasterLookup : {}", id);
        masterLookupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
