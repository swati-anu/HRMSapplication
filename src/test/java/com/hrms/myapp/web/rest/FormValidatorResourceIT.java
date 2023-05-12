package com.hrms.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrms.myapp.IntegrationTest;
import com.hrms.myapp.domain.FormValidator;
import com.hrms.myapp.repository.FormValidatorRepository;
import com.hrms.myapp.service.criteria.FormValidatorCriteria;
import com.hrms.myapp.service.dto.FormValidatorDTO;
import com.hrms.myapp.service.mapper.FormValidatorMapper;
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
 * Integration tests for the {@link FormValidatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormValidatorResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FORM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;
    private static final Long SMALLER_COMPANY_ID = 1L - 1L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/form-validators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormValidatorRepository formValidatorRepository;

    @Autowired
    private FormValidatorMapper formValidatorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormValidatorMockMvc;

    private FormValidator formValidator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormValidator createEntity(EntityManager em) {
        FormValidator formValidator = new FormValidator()
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE)
            .formName(DEFAULT_FORM_NAME)
            .fieldName(DEFAULT_FIELD_NAME)
            .companyId(DEFAULT_COMPANY_ID)
            .status(DEFAULT_STATUS)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return formValidator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormValidator createUpdatedEntity(EntityManager em) {
        FormValidator formValidator = new FormValidator()
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .formName(UPDATED_FORM_NAME)
            .fieldName(UPDATED_FIELD_NAME)
            .companyId(UPDATED_COMPANY_ID)
            .status(UPDATED_STATUS)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return formValidator;
    }

    @BeforeEach
    public void initTest() {
        formValidator = createEntity(em);
    }

    @Test
    @Transactional
    void createFormValidator() throws Exception {
        int databaseSizeBeforeCreate = formValidatorRepository.findAll().size();
        // Create the FormValidator
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(formValidator);
        restFormValidatorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeCreate + 1);
        FormValidator testFormValidator = formValidatorList.get(formValidatorList.size() - 1);
        assertThat(testFormValidator.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFormValidator.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testFormValidator.getFormName()).isEqualTo(DEFAULT_FORM_NAME);
        assertThat(testFormValidator.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testFormValidator.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testFormValidator.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFormValidator.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testFormValidator.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createFormValidatorWithExistingId() throws Exception {
        // Create the FormValidator with an existing ID
        formValidator.setId(1L);
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(formValidator);

        int databaseSizeBeforeCreate = formValidatorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormValidatorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormValidators() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList
        restFormValidatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formValidator.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].formName").value(hasItem(DEFAULT_FORM_NAME)))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getFormValidator() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get the formValidator
        restFormValidatorMockMvc
            .perform(get(ENTITY_API_URL_ID, formValidator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formValidator.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.formName").value(DEFAULT_FORM_NAME))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getFormValidatorsByIdFiltering() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        Long id = formValidator.getId();

        defaultFormValidatorShouldBeFound("id.equals=" + id);
        defaultFormValidatorShouldNotBeFound("id.notEquals=" + id);

        defaultFormValidatorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormValidatorShouldNotBeFound("id.greaterThan=" + id);

        defaultFormValidatorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormValidatorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where type equals to DEFAULT_TYPE
        defaultFormValidatorShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the formValidatorList where type equals to UPDATED_TYPE
        defaultFormValidatorShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultFormValidatorShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the formValidatorList where type equals to UPDATED_TYPE
        defaultFormValidatorShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where type is not null
        defaultFormValidatorShouldBeFound("type.specified=true");

        // Get all the formValidatorList where type is null
        defaultFormValidatorShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllFormValidatorsByTypeContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where type contains DEFAULT_TYPE
        defaultFormValidatorShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the formValidatorList where type contains UPDATED_TYPE
        defaultFormValidatorShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where type does not contain DEFAULT_TYPE
        defaultFormValidatorShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the formValidatorList where type does not contain UPDATED_TYPE
        defaultFormValidatorShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where value equals to DEFAULT_VALUE
        defaultFormValidatorShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the formValidatorList where value equals to UPDATED_VALUE
        defaultFormValidatorShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultFormValidatorShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the formValidatorList where value equals to UPDATED_VALUE
        defaultFormValidatorShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where value is not null
        defaultFormValidatorShouldBeFound("value.specified=true");

        // Get all the formValidatorList where value is null
        defaultFormValidatorShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllFormValidatorsByValueContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where value contains DEFAULT_VALUE
        defaultFormValidatorShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the formValidatorList where value contains UPDATED_VALUE
        defaultFormValidatorShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where value does not contain DEFAULT_VALUE
        defaultFormValidatorShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the formValidatorList where value does not contain UPDATED_VALUE
        defaultFormValidatorShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFormNameIsEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where formName equals to DEFAULT_FORM_NAME
        defaultFormValidatorShouldBeFound("formName.equals=" + DEFAULT_FORM_NAME);

        // Get all the formValidatorList where formName equals to UPDATED_FORM_NAME
        defaultFormValidatorShouldNotBeFound("formName.equals=" + UPDATED_FORM_NAME);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFormNameIsInShouldWork() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where formName in DEFAULT_FORM_NAME or UPDATED_FORM_NAME
        defaultFormValidatorShouldBeFound("formName.in=" + DEFAULT_FORM_NAME + "," + UPDATED_FORM_NAME);

        // Get all the formValidatorList where formName equals to UPDATED_FORM_NAME
        defaultFormValidatorShouldNotBeFound("formName.in=" + UPDATED_FORM_NAME);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFormNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where formName is not null
        defaultFormValidatorShouldBeFound("formName.specified=true");

        // Get all the formValidatorList where formName is null
        defaultFormValidatorShouldNotBeFound("formName.specified=false");
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFormNameContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where formName contains DEFAULT_FORM_NAME
        defaultFormValidatorShouldBeFound("formName.contains=" + DEFAULT_FORM_NAME);

        // Get all the formValidatorList where formName contains UPDATED_FORM_NAME
        defaultFormValidatorShouldNotBeFound("formName.contains=" + UPDATED_FORM_NAME);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFormNameNotContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where formName does not contain DEFAULT_FORM_NAME
        defaultFormValidatorShouldNotBeFound("formName.doesNotContain=" + DEFAULT_FORM_NAME);

        // Get all the formValidatorList where formName does not contain UPDATED_FORM_NAME
        defaultFormValidatorShouldBeFound("formName.doesNotContain=" + UPDATED_FORM_NAME);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFieldNameIsEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where fieldName equals to DEFAULT_FIELD_NAME
        defaultFormValidatorShouldBeFound("fieldName.equals=" + DEFAULT_FIELD_NAME);

        // Get all the formValidatorList where fieldName equals to UPDATED_FIELD_NAME
        defaultFormValidatorShouldNotBeFound("fieldName.equals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFieldNameIsInShouldWork() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where fieldName in DEFAULT_FIELD_NAME or UPDATED_FIELD_NAME
        defaultFormValidatorShouldBeFound("fieldName.in=" + DEFAULT_FIELD_NAME + "," + UPDATED_FIELD_NAME);

        // Get all the formValidatorList where fieldName equals to UPDATED_FIELD_NAME
        defaultFormValidatorShouldNotBeFound("fieldName.in=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFieldNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where fieldName is not null
        defaultFormValidatorShouldBeFound("fieldName.specified=true");

        // Get all the formValidatorList where fieldName is null
        defaultFormValidatorShouldNotBeFound("fieldName.specified=false");
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFieldNameContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where fieldName contains DEFAULT_FIELD_NAME
        defaultFormValidatorShouldBeFound("fieldName.contains=" + DEFAULT_FIELD_NAME);

        // Get all the formValidatorList where fieldName contains UPDATED_FIELD_NAME
        defaultFormValidatorShouldNotBeFound("fieldName.contains=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByFieldNameNotContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where fieldName does not contain DEFAULT_FIELD_NAME
        defaultFormValidatorShouldNotBeFound("fieldName.doesNotContain=" + DEFAULT_FIELD_NAME);

        // Get all the formValidatorList where fieldName does not contain UPDATED_FIELD_NAME
        defaultFormValidatorShouldBeFound("fieldName.doesNotContain=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where companyId equals to DEFAULT_COMPANY_ID
        defaultFormValidatorShouldBeFound("companyId.equals=" + DEFAULT_COMPANY_ID);

        // Get all the formValidatorList where companyId equals to UPDATED_COMPANY_ID
        defaultFormValidatorShouldNotBeFound("companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where companyId in DEFAULT_COMPANY_ID or UPDATED_COMPANY_ID
        defaultFormValidatorShouldBeFound("companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID);

        // Get all the formValidatorList where companyId equals to UPDATED_COMPANY_ID
        defaultFormValidatorShouldNotBeFound("companyId.in=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where companyId is not null
        defaultFormValidatorShouldBeFound("companyId.specified=true");

        // Get all the formValidatorList where companyId is null
        defaultFormValidatorShouldNotBeFound("companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllFormValidatorsByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where companyId is greater than or equal to DEFAULT_COMPANY_ID
        defaultFormValidatorShouldBeFound("companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the formValidatorList where companyId is greater than or equal to UPDATED_COMPANY_ID
        defaultFormValidatorShouldNotBeFound("companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where companyId is less than or equal to DEFAULT_COMPANY_ID
        defaultFormValidatorShouldBeFound("companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the formValidatorList where companyId is less than or equal to SMALLER_COMPANY_ID
        defaultFormValidatorShouldNotBeFound("companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where companyId is less than DEFAULT_COMPANY_ID
        defaultFormValidatorShouldNotBeFound("companyId.lessThan=" + DEFAULT_COMPANY_ID);

        // Get all the formValidatorList where companyId is less than UPDATED_COMPANY_ID
        defaultFormValidatorShouldBeFound("companyId.lessThan=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where companyId is greater than DEFAULT_COMPANY_ID
        defaultFormValidatorShouldNotBeFound("companyId.greaterThan=" + DEFAULT_COMPANY_ID);

        // Get all the formValidatorList where companyId is greater than SMALLER_COMPANY_ID
        defaultFormValidatorShouldBeFound("companyId.greaterThan=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where status equals to DEFAULT_STATUS
        defaultFormValidatorShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the formValidatorList where status equals to UPDATED_STATUS
        defaultFormValidatorShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultFormValidatorShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the formValidatorList where status equals to UPDATED_STATUS
        defaultFormValidatorShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where status is not null
        defaultFormValidatorShouldBeFound("status.specified=true");

        // Get all the formValidatorList where status is null
        defaultFormValidatorShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllFormValidatorsByStatusContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where status contains DEFAULT_STATUS
        defaultFormValidatorShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the formValidatorList where status contains UPDATED_STATUS
        defaultFormValidatorShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where status does not contain DEFAULT_STATUS
        defaultFormValidatorShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the formValidatorList where status does not contain UPDATED_STATUS
        defaultFormValidatorShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultFormValidatorShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the formValidatorList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultFormValidatorShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultFormValidatorShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the formValidatorList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultFormValidatorShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where lastModified is not null
        defaultFormValidatorShouldBeFound("lastModified.specified=true");

        // Get all the formValidatorList where lastModified is null
        defaultFormValidatorShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllFormValidatorsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultFormValidatorShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the formValidatorList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultFormValidatorShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultFormValidatorShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the formValidatorList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultFormValidatorShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where lastModifiedBy is not null
        defaultFormValidatorShouldBeFound("lastModifiedBy.specified=true");

        // Get all the formValidatorList where lastModifiedBy is null
        defaultFormValidatorShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFormValidatorsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultFormValidatorShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the formValidatorList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultFormValidatorShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllFormValidatorsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        // Get all the formValidatorList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultFormValidatorShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the formValidatorList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultFormValidatorShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormValidatorShouldBeFound(String filter) throws Exception {
        restFormValidatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formValidator.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].formName").value(hasItem(DEFAULT_FORM_NAME)))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restFormValidatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormValidatorShouldNotBeFound(String filter) throws Exception {
        restFormValidatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormValidatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormValidator() throws Exception {
        // Get the formValidator
        restFormValidatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormValidator() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();

        // Update the formValidator
        FormValidator updatedFormValidator = formValidatorRepository.findById(formValidator.getId()).get();
        // Disconnect from session so that the updates on updatedFormValidator are not directly saved in db
        em.detach(updatedFormValidator);
        updatedFormValidator
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .formName(UPDATED_FORM_NAME)
            .fieldName(UPDATED_FIELD_NAME)
            .companyId(UPDATED_COMPANY_ID)
            .status(UPDATED_STATUS)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(updatedFormValidator);

        restFormValidatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formValidatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
        FormValidator testFormValidator = formValidatorList.get(formValidatorList.size() - 1);
        assertThat(testFormValidator.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFormValidator.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testFormValidator.getFormName()).isEqualTo(UPDATED_FORM_NAME);
        assertThat(testFormValidator.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testFormValidator.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testFormValidator.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFormValidator.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testFormValidator.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingFormValidator() throws Exception {
        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();
        formValidator.setId(count.incrementAndGet());

        // Create the FormValidator
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(formValidator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormValidatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formValidatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormValidator() throws Exception {
        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();
        formValidator.setId(count.incrementAndGet());

        // Create the FormValidator
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(formValidator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormValidatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormValidator() throws Exception {
        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();
        formValidator.setId(count.incrementAndGet());

        // Create the FormValidator
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(formValidator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormValidatorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormValidatorWithPatch() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();

        // Update the formValidator using partial update
        FormValidator partialUpdatedFormValidator = new FormValidator();
        partialUpdatedFormValidator.setId(formValidator.getId());

        partialUpdatedFormValidator.value(UPDATED_VALUE).lastModified(UPDATED_LAST_MODIFIED).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restFormValidatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormValidator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormValidator))
            )
            .andExpect(status().isOk());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
        FormValidator testFormValidator = formValidatorList.get(formValidatorList.size() - 1);
        assertThat(testFormValidator.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFormValidator.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testFormValidator.getFormName()).isEqualTo(DEFAULT_FORM_NAME);
        assertThat(testFormValidator.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testFormValidator.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testFormValidator.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFormValidator.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testFormValidator.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateFormValidatorWithPatch() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();

        // Update the formValidator using partial update
        FormValidator partialUpdatedFormValidator = new FormValidator();
        partialUpdatedFormValidator.setId(formValidator.getId());

        partialUpdatedFormValidator
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .formName(UPDATED_FORM_NAME)
            .fieldName(UPDATED_FIELD_NAME)
            .companyId(UPDATED_COMPANY_ID)
            .status(UPDATED_STATUS)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restFormValidatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormValidator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormValidator))
            )
            .andExpect(status().isOk());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
        FormValidator testFormValidator = formValidatorList.get(formValidatorList.size() - 1);
        assertThat(testFormValidator.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFormValidator.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testFormValidator.getFormName()).isEqualTo(UPDATED_FORM_NAME);
        assertThat(testFormValidator.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testFormValidator.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testFormValidator.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFormValidator.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testFormValidator.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingFormValidator() throws Exception {
        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();
        formValidator.setId(count.incrementAndGet());

        // Create the FormValidator
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(formValidator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormValidatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formValidatorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormValidator() throws Exception {
        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();
        formValidator.setId(count.incrementAndGet());

        // Create the FormValidator
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(formValidator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormValidatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormValidator() throws Exception {
        int databaseSizeBeforeUpdate = formValidatorRepository.findAll().size();
        formValidator.setId(count.incrementAndGet());

        // Create the FormValidator
        FormValidatorDTO formValidatorDTO = formValidatorMapper.toDto(formValidator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormValidatorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formValidatorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormValidator in the database
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormValidator() throws Exception {
        // Initialize the database
        formValidatorRepository.saveAndFlush(formValidator);

        int databaseSizeBeforeDelete = formValidatorRepository.findAll().size();

        // Delete the formValidator
        restFormValidatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, formValidator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormValidator> formValidatorList = formValidatorRepository.findAll();
        assertThat(formValidatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
