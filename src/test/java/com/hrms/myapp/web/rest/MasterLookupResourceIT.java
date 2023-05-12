package com.hrms.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrms.myapp.IntegrationTest;
import com.hrms.myapp.domain.MasterLookup;
import com.hrms.myapp.repository.MasterLookupRepository;
import com.hrms.myapp.service.criteria.MasterLookupCriteria;
import com.hrms.myapp.service.dto.MasterLookupDTO;
import com.hrms.myapp.service.mapper.MasterLookupMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MasterLookupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MasterLookupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_TWO = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_TWO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;
    private static final Long SMALLER_COMPANY_ID = 1L - 1L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/master-lookups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MasterLookupRepository masterLookupRepository;

    @Autowired
    private MasterLookupMapper masterLookupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMasterLookupMockMvc;

    private MasterLookup masterLookup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterLookup createEntity(EntityManager em) {
        MasterLookup masterLookup = new MasterLookup()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .valueTwo(DEFAULT_VALUE_TWO)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .companyId(DEFAULT_COMPANY_ID)
            .status(DEFAULT_STATUS)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return masterLookup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterLookup createUpdatedEntity(EntityManager em) {
        MasterLookup masterLookup = new MasterLookup()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueTwo(UPDATED_VALUE_TWO)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .companyId(UPDATED_COMPANY_ID)
            .status(UPDATED_STATUS)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return masterLookup;
    }

    @BeforeEach
    public void initTest() {
        masterLookup = createEntity(em);
    }

    @Test
    @Transactional
    void createMasterLookup() throws Exception {
        int databaseSizeBeforeCreate = masterLookupRepository.findAll().size();
        // Create the MasterLookup
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(masterLookup);
        restMasterLookupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeCreate + 1);
        MasterLookup testMasterLookup = masterLookupList.get(masterLookupList.size() - 1);
        assertThat(testMasterLookup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMasterLookup.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testMasterLookup.getValueTwo()).isEqualTo(DEFAULT_VALUE_TWO);
        assertThat(testMasterLookup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMasterLookup.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMasterLookup.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testMasterLookup.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMasterLookup.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testMasterLookup.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createMasterLookupWithExistingId() throws Exception {
        // Create the MasterLookup with an existing ID
        masterLookup.setId(1L);
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(masterLookup);

        int databaseSizeBeforeCreate = masterLookupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterLookupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMasterLookups() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList
        restMasterLookupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterLookup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueTwo").value(hasItem(DEFAULT_VALUE_TWO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getMasterLookup() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get the masterLookup
        restMasterLookupMockMvc
            .perform(get(ENTITY_API_URL_ID, masterLookup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(masterLookup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueTwo").value(DEFAULT_VALUE_TWO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getMasterLookupsByIdFiltering() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        Long id = masterLookup.getId();

        defaultMasterLookupShouldBeFound("id.equals=" + id);
        defaultMasterLookupShouldNotBeFound("id.notEquals=" + id);

        defaultMasterLookupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMasterLookupShouldNotBeFound("id.greaterThan=" + id);

        defaultMasterLookupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMasterLookupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where name equals to DEFAULT_NAME
        defaultMasterLookupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the masterLookupList where name equals to UPDATED_NAME
        defaultMasterLookupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMasterLookupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the masterLookupList where name equals to UPDATED_NAME
        defaultMasterLookupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where name is not null
        defaultMasterLookupShouldBeFound("name.specified=true");

        // Get all the masterLookupList where name is null
        defaultMasterLookupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByNameContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where name contains DEFAULT_NAME
        defaultMasterLookupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the masterLookupList where name contains UPDATED_NAME
        defaultMasterLookupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where name does not contain DEFAULT_NAME
        defaultMasterLookupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the masterLookupList where name does not contain UPDATED_NAME
        defaultMasterLookupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where value equals to DEFAULT_VALUE
        defaultMasterLookupShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the masterLookupList where value equals to UPDATED_VALUE
        defaultMasterLookupShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultMasterLookupShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the masterLookupList where value equals to UPDATED_VALUE
        defaultMasterLookupShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where value is not null
        defaultMasterLookupShouldBeFound("value.specified=true");

        // Get all the masterLookupList where value is null
        defaultMasterLookupShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where value contains DEFAULT_VALUE
        defaultMasterLookupShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the masterLookupList where value contains UPDATED_VALUE
        defaultMasterLookupShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where value does not contain DEFAULT_VALUE
        defaultMasterLookupShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the masterLookupList where value does not contain UPDATED_VALUE
        defaultMasterLookupShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueTwoIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where valueTwo equals to DEFAULT_VALUE_TWO
        defaultMasterLookupShouldBeFound("valueTwo.equals=" + DEFAULT_VALUE_TWO);

        // Get all the masterLookupList where valueTwo equals to UPDATED_VALUE_TWO
        defaultMasterLookupShouldNotBeFound("valueTwo.equals=" + UPDATED_VALUE_TWO);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueTwoIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where valueTwo in DEFAULT_VALUE_TWO or UPDATED_VALUE_TWO
        defaultMasterLookupShouldBeFound("valueTwo.in=" + DEFAULT_VALUE_TWO + "," + UPDATED_VALUE_TWO);

        // Get all the masterLookupList where valueTwo equals to UPDATED_VALUE_TWO
        defaultMasterLookupShouldNotBeFound("valueTwo.in=" + UPDATED_VALUE_TWO);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueTwoIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where valueTwo is not null
        defaultMasterLookupShouldBeFound("valueTwo.specified=true");

        // Get all the masterLookupList where valueTwo is null
        defaultMasterLookupShouldNotBeFound("valueTwo.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueTwoContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where valueTwo contains DEFAULT_VALUE_TWO
        defaultMasterLookupShouldBeFound("valueTwo.contains=" + DEFAULT_VALUE_TWO);

        // Get all the masterLookupList where valueTwo contains UPDATED_VALUE_TWO
        defaultMasterLookupShouldNotBeFound("valueTwo.contains=" + UPDATED_VALUE_TWO);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByValueTwoNotContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where valueTwo does not contain DEFAULT_VALUE_TWO
        defaultMasterLookupShouldNotBeFound("valueTwo.doesNotContain=" + DEFAULT_VALUE_TWO);

        // Get all the masterLookupList where valueTwo does not contain UPDATED_VALUE_TWO
        defaultMasterLookupShouldBeFound("valueTwo.doesNotContain=" + UPDATED_VALUE_TWO);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where description equals to DEFAULT_DESCRIPTION
        defaultMasterLookupShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the masterLookupList where description equals to UPDATED_DESCRIPTION
        defaultMasterLookupShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMasterLookupShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the masterLookupList where description equals to UPDATED_DESCRIPTION
        defaultMasterLookupShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where description is not null
        defaultMasterLookupShouldBeFound("description.specified=true");

        // Get all the masterLookupList where description is null
        defaultMasterLookupShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where description contains DEFAULT_DESCRIPTION
        defaultMasterLookupShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the masterLookupList where description contains UPDATED_DESCRIPTION
        defaultMasterLookupShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where description does not contain DEFAULT_DESCRIPTION
        defaultMasterLookupShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the masterLookupList where description does not contain UPDATED_DESCRIPTION
        defaultMasterLookupShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where type equals to DEFAULT_TYPE
        defaultMasterLookupShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the masterLookupList where type equals to UPDATED_TYPE
        defaultMasterLookupShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultMasterLookupShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the masterLookupList where type equals to UPDATED_TYPE
        defaultMasterLookupShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where type is not null
        defaultMasterLookupShouldBeFound("type.specified=true");

        // Get all the masterLookupList where type is null
        defaultMasterLookupShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByTypeContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where type contains DEFAULT_TYPE
        defaultMasterLookupShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the masterLookupList where type contains UPDATED_TYPE
        defaultMasterLookupShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where type does not contain DEFAULT_TYPE
        defaultMasterLookupShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the masterLookupList where type does not contain UPDATED_TYPE
        defaultMasterLookupShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where companyId equals to DEFAULT_COMPANY_ID
        defaultMasterLookupShouldBeFound("companyId.equals=" + DEFAULT_COMPANY_ID);

        // Get all the masterLookupList where companyId equals to UPDATED_COMPANY_ID
        defaultMasterLookupShouldNotBeFound("companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where companyId in DEFAULT_COMPANY_ID or UPDATED_COMPANY_ID
        defaultMasterLookupShouldBeFound("companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID);

        // Get all the masterLookupList where companyId equals to UPDATED_COMPANY_ID
        defaultMasterLookupShouldNotBeFound("companyId.in=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where companyId is not null
        defaultMasterLookupShouldBeFound("companyId.specified=true");

        // Get all the masterLookupList where companyId is null
        defaultMasterLookupShouldNotBeFound("companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where companyId is greater than or equal to DEFAULT_COMPANY_ID
        defaultMasterLookupShouldBeFound("companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the masterLookupList where companyId is greater than or equal to UPDATED_COMPANY_ID
        defaultMasterLookupShouldNotBeFound("companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where companyId is less than or equal to DEFAULT_COMPANY_ID
        defaultMasterLookupShouldBeFound("companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the masterLookupList where companyId is less than or equal to SMALLER_COMPANY_ID
        defaultMasterLookupShouldNotBeFound("companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where companyId is less than DEFAULT_COMPANY_ID
        defaultMasterLookupShouldNotBeFound("companyId.lessThan=" + DEFAULT_COMPANY_ID);

        // Get all the masterLookupList where companyId is less than UPDATED_COMPANY_ID
        defaultMasterLookupShouldBeFound("companyId.lessThan=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where companyId is greater than DEFAULT_COMPANY_ID
        defaultMasterLookupShouldNotBeFound("companyId.greaterThan=" + DEFAULT_COMPANY_ID);

        // Get all the masterLookupList where companyId is greater than SMALLER_COMPANY_ID
        defaultMasterLookupShouldBeFound("companyId.greaterThan=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where status equals to DEFAULT_STATUS
        defaultMasterLookupShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the masterLookupList where status equals to UPDATED_STATUS
        defaultMasterLookupShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMasterLookupShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the masterLookupList where status equals to UPDATED_STATUS
        defaultMasterLookupShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where status is not null
        defaultMasterLookupShouldBeFound("status.specified=true");

        // Get all the masterLookupList where status is null
        defaultMasterLookupShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByStatusContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where status contains DEFAULT_STATUS
        defaultMasterLookupShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the masterLookupList where status contains UPDATED_STATUS
        defaultMasterLookupShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where status does not contain DEFAULT_STATUS
        defaultMasterLookupShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the masterLookupList where status does not contain UPDATED_STATUS
        defaultMasterLookupShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultMasterLookupShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the masterLookupList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultMasterLookupShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultMasterLookupShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the masterLookupList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultMasterLookupShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where lastModified is not null
        defaultMasterLookupShouldBeFound("lastModified.specified=true");

        // Get all the masterLookupList where lastModified is null
        defaultMasterLookupShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultMasterLookupShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the masterLookupList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultMasterLookupShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultMasterLookupShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the masterLookupList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultMasterLookupShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where lastModifiedBy is not null
        defaultMasterLookupShouldBeFound("lastModifiedBy.specified=true");

        // Get all the masterLookupList where lastModifiedBy is null
        defaultMasterLookupShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterLookupsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultMasterLookupShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the masterLookupList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultMasterLookupShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMasterLookupsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        // Get all the masterLookupList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultMasterLookupShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the masterLookupList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultMasterLookupShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMasterLookupShouldBeFound(String filter) throws Exception {
        restMasterLookupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterLookup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueTwo").value(hasItem(DEFAULT_VALUE_TWO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restMasterLookupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMasterLookupShouldNotBeFound(String filter) throws Exception {
        restMasterLookupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMasterLookupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMasterLookup() throws Exception {
        // Get the masterLookup
        restMasterLookupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMasterLookup() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();

        // Update the masterLookup
        MasterLookup updatedMasterLookup = masterLookupRepository.findById(masterLookup.getId()).get();
        // Disconnect from session so that the updates on updatedMasterLookup are not directly saved in db
        em.detach(updatedMasterLookup);
        updatedMasterLookup
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueTwo(UPDATED_VALUE_TWO)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .companyId(UPDATED_COMPANY_ID)
            .status(UPDATED_STATUS)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(updatedMasterLookup);

        restMasterLookupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterLookupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isOk());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
        MasterLookup testMasterLookup = masterLookupList.get(masterLookupList.size() - 1);
        assertThat(testMasterLookup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterLookup.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMasterLookup.getValueTwo()).isEqualTo(UPDATED_VALUE_TWO);
        assertThat(testMasterLookup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterLookup.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMasterLookup.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testMasterLookup.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMasterLookup.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testMasterLookup.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingMasterLookup() throws Exception {
        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();
        masterLookup.setId(count.incrementAndGet());

        // Create the MasterLookup
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(masterLookup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterLookupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterLookupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMasterLookup() throws Exception {
        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();
        masterLookup.setId(count.incrementAndGet());

        // Create the MasterLookup
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(masterLookup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterLookupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMasterLookup() throws Exception {
        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();
        masterLookup.setId(count.incrementAndGet());

        // Create the MasterLookup
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(masterLookup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterLookupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMasterLookupWithPatch() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();

        // Update the masterLookup using partial update
        MasterLookup partialUpdatedMasterLookup = new MasterLookup();
        partialUpdatedMasterLookup.setId(masterLookup.getId());

        partialUpdatedMasterLookup
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restMasterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterLookup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterLookup))
            )
            .andExpect(status().isOk());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
        MasterLookup testMasterLookup = masterLookupList.get(masterLookupList.size() - 1);
        assertThat(testMasterLookup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMasterLookup.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMasterLookup.getValueTwo()).isEqualTo(DEFAULT_VALUE_TWO);
        assertThat(testMasterLookup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterLookup.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMasterLookup.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testMasterLookup.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMasterLookup.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testMasterLookup.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateMasterLookupWithPatch() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();

        // Update the masterLookup using partial update
        MasterLookup partialUpdatedMasterLookup = new MasterLookup();
        partialUpdatedMasterLookup.setId(masterLookup.getId());

        partialUpdatedMasterLookup
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueTwo(UPDATED_VALUE_TWO)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .companyId(UPDATED_COMPANY_ID)
            .status(UPDATED_STATUS)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restMasterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterLookup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterLookup))
            )
            .andExpect(status().isOk());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
        MasterLookup testMasterLookup = masterLookupList.get(masterLookupList.size() - 1);
        assertThat(testMasterLookup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterLookup.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMasterLookup.getValueTwo()).isEqualTo(UPDATED_VALUE_TWO);
        assertThat(testMasterLookup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterLookup.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMasterLookup.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testMasterLookup.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMasterLookup.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testMasterLookup.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingMasterLookup() throws Exception {
        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();
        masterLookup.setId(count.incrementAndGet());

        // Create the MasterLookup
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(masterLookup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, masterLookupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMasterLookup() throws Exception {
        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();
        masterLookup.setId(count.incrementAndGet());

        // Create the MasterLookup
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(masterLookup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMasterLookup() throws Exception {
        int databaseSizeBeforeUpdate = masterLookupRepository.findAll().size();
        masterLookup.setId(count.incrementAndGet());

        // Create the MasterLookup
        MasterLookupDTO masterLookupDTO = masterLookupMapper.toDto(masterLookup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterLookupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterLookupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterLookup in the database
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMasterLookup() throws Exception {
        // Initialize the database
        masterLookupRepository.saveAndFlush(masterLookup);

        int databaseSizeBeforeDelete = masterLookupRepository.findAll().size();

        // Delete the masterLookup
        restMasterLookupMockMvc
            .perform(delete(ENTITY_API_URL_ID, masterLookup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MasterLookup> masterLookupList = masterLookupRepository.findAll();
        assertThat(masterLookupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
