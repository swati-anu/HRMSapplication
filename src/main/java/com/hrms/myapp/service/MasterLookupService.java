package com.hrms.myapp.service;

import com.hrms.myapp.domain.MasterLookup;
import com.hrms.myapp.repository.MasterLookupRepository;
import com.hrms.myapp.service.dto.MasterLookupDTO;
import com.hrms.myapp.service.mapper.MasterLookupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MasterLookup}.
 */
@Service
@Transactional
public class MasterLookupService {

    private final Logger log = LoggerFactory.getLogger(MasterLookupService.class);

    private final MasterLookupRepository masterLookupRepository;

    private final MasterLookupMapper masterLookupMapper;

    public MasterLookupService(MasterLookupRepository masterLookupRepository, MasterLookupMapper masterLookupMapper) {
        this.masterLookupRepository = masterLookupRepository;
        this.masterLookupMapper = masterLookupMapper;
    }

    /**
     * Save a masterLookup.
     *
     * @param masterLookupDTO the entity to save.
     * @return the persisted entity.
     */
    public MasterLookupDTO save(MasterLookupDTO masterLookupDTO) {
        log.debug("Request to save MasterLookup : {}", masterLookupDTO);
        MasterLookup masterLookup = masterLookupMapper.toEntity(masterLookupDTO);
        masterLookup = masterLookupRepository.save(masterLookup);
        return masterLookupMapper.toDto(masterLookup);
    }

    /**
     * Update a masterLookup.
     *
     * @param masterLookupDTO the entity to save.
     * @return the persisted entity.
     */
    public MasterLookupDTO update(MasterLookupDTO masterLookupDTO) {
        log.debug("Request to update MasterLookup : {}", masterLookupDTO);
        MasterLookup masterLookup = masterLookupMapper.toEntity(masterLookupDTO);
        masterLookup = masterLookupRepository.save(masterLookup);
        return masterLookupMapper.toDto(masterLookup);
    }

    /**
     * Partially update a masterLookup.
     *
     * @param masterLookupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MasterLookupDTO> partialUpdate(MasterLookupDTO masterLookupDTO) {
        log.debug("Request to partially update MasterLookup : {}", masterLookupDTO);

        return masterLookupRepository
            .findById(masterLookupDTO.getId())
            .map(existingMasterLookup -> {
                masterLookupMapper.partialUpdate(existingMasterLookup, masterLookupDTO);

                return existingMasterLookup;
            })
            .map(masterLookupRepository::save)
            .map(masterLookupMapper::toDto);
    }

    /**
     * Get all the masterLookups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MasterLookupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MasterLookups");
        return masterLookupRepository.findAll(pageable).map(masterLookupMapper::toDto);
    }

    /**
     * Get one masterLookup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MasterLookupDTO> findOne(Long id) {
        log.debug("Request to get MasterLookup : {}", id);
        return masterLookupRepository.findById(id).map(masterLookupMapper::toDto);
    }

    /**
     * Delete the masterLookup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MasterLookup : {}", id);
        masterLookupRepository.deleteById(id);
    }
}
