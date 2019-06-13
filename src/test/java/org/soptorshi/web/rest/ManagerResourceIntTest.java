package org.soptorshi.web.rest;

import org.soptorshi.SoptorshiApp;

import org.soptorshi.domain.Manager;
import org.soptorshi.repository.ManagerRepository;
import org.soptorshi.repository.search.ManagerSearchRepository;
import org.soptorshi.service.ManagerService;
import org.soptorshi.service.dto.ManagerDTO;
import org.soptorshi.service.mapper.ManagerMapper;
import org.soptorshi.web.rest.errors.ExceptionTranslator;
import org.soptorshi.service.dto.ManagerCriteria;
import org.soptorshi.service.ManagerQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static org.soptorshi.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ManagerResource REST controller.
 *
 * @see ManagerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SoptorshiApp.class)
public class ManagerResourceIntTest {

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;

    private static final Long DEFAULT_MANAGER_ID = 1L;
    private static final Long UPDATED_MANAGER_ID = 2L;

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private ManagerService managerService;

    /**
     * This repository is mocked in the org.soptorshi.repository.search test package.
     *
     * @see org.soptorshi.repository.search.ManagerSearchRepositoryMockConfiguration
     */
    @Autowired
    private ManagerSearchRepository mockManagerSearchRepository;

    @Autowired
    private ManagerQueryService managerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restManagerMockMvc;

    private Manager manager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ManagerResource managerResource = new ManagerResource(managerService, managerQueryService);
        this.restManagerMockMvc = MockMvcBuilders.standaloneSetup(managerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manager createEntity(EntityManager em) {
        Manager manager = new Manager()
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .managerId(DEFAULT_MANAGER_ID)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedOn(DEFAULT_MODIFIED_ON);
        return manager;
    }

    @Before
    public void initTest() {
        manager = createEntity(em);
    }

    @Test
    @Transactional
    public void createManager() throws Exception {
        int databaseSizeBeforeCreate = managerRepository.findAll().size();

        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);
        restManagerMockMvc.perform(post("/api/managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(managerDTO)))
            .andExpect(status().isCreated());

        // Validate the Manager in the database
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeCreate + 1);
        Manager testManager = managerList.get(managerList.size() - 1);
        assertThat(testManager.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testManager.getManagerId()).isEqualTo(DEFAULT_MANAGER_ID);
        assertThat(testManager.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testManager.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);

        // Validate the Manager in Elasticsearch
        verify(mockManagerSearchRepository, times(1)).save(testManager);
    }

    @Test
    @Transactional
    public void createManagerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = managerRepository.findAll().size();

        // Create the Manager with an existing ID
        manager.setId(1L);
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        // An entity with an existing ID cannot be created, so this API call must fail
        restManagerMockMvc.perform(post("/api/managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(managerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Manager in the database
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Manager in Elasticsearch
        verify(mockManagerSearchRepository, times(0)).save(manager);
    }

    @Test
    @Transactional
    public void getAllManagers() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList
        restManagerMockMvc.perform(get("/api/managers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manager.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].managerId").value(hasItem(DEFAULT_MANAGER_ID.intValue())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getManager() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get the manager
        restManagerMockMvc.perform(get("/api/managers/{id}", manager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(manager.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.managerId").value(DEFAULT_MANAGER_ID.intValue()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()));
    }

    @Test
    @Transactional
    public void getAllManagersByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultManagerShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the managerList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultManagerShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllManagersByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultManagerShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the managerList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultManagerShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllManagersByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where employeeId is not null
        defaultManagerShouldBeFound("employeeId.specified=true");

        // Get all the managerList where employeeId is null
        defaultManagerShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllManagersByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where employeeId greater than or equals to DEFAULT_EMPLOYEE_ID
        defaultManagerShouldBeFound("employeeId.greaterOrEqualThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the managerList where employeeId greater than or equals to UPDATED_EMPLOYEE_ID
        defaultManagerShouldNotBeFound("employeeId.greaterOrEqualThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllManagersByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where employeeId less than or equals to DEFAULT_EMPLOYEE_ID
        defaultManagerShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the managerList where employeeId less than or equals to UPDATED_EMPLOYEE_ID
        defaultManagerShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }


    @Test
    @Transactional
    public void getAllManagersByManagerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where managerId equals to DEFAULT_MANAGER_ID
        defaultManagerShouldBeFound("managerId.equals=" + DEFAULT_MANAGER_ID);

        // Get all the managerList where managerId equals to UPDATED_MANAGER_ID
        defaultManagerShouldNotBeFound("managerId.equals=" + UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    public void getAllManagersByManagerIdIsInShouldWork() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where managerId in DEFAULT_MANAGER_ID or UPDATED_MANAGER_ID
        defaultManagerShouldBeFound("managerId.in=" + DEFAULT_MANAGER_ID + "," + UPDATED_MANAGER_ID);

        // Get all the managerList where managerId equals to UPDATED_MANAGER_ID
        defaultManagerShouldNotBeFound("managerId.in=" + UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    public void getAllManagersByManagerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where managerId is not null
        defaultManagerShouldBeFound("managerId.specified=true");

        // Get all the managerList where managerId is null
        defaultManagerShouldNotBeFound("managerId.specified=false");
    }

    @Test
    @Transactional
    public void getAllManagersByManagerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where managerId greater than or equals to DEFAULT_MANAGER_ID
        defaultManagerShouldBeFound("managerId.greaterOrEqualThan=" + DEFAULT_MANAGER_ID);

        // Get all the managerList where managerId greater than or equals to UPDATED_MANAGER_ID
        defaultManagerShouldNotBeFound("managerId.greaterOrEqualThan=" + UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    public void getAllManagersByManagerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where managerId less than or equals to DEFAULT_MANAGER_ID
        defaultManagerShouldNotBeFound("managerId.lessThan=" + DEFAULT_MANAGER_ID);

        // Get all the managerList where managerId less than or equals to UPDATED_MANAGER_ID
        defaultManagerShouldBeFound("managerId.lessThan=" + UPDATED_MANAGER_ID);
    }


    @Test
    @Transactional
    public void getAllManagersByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultManagerShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the managerList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultManagerShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllManagersByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultManagerShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the managerList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultManagerShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllManagersByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where modifiedBy is not null
        defaultManagerShouldBeFound("modifiedBy.specified=true");

        // Get all the managerList where modifiedBy is null
        defaultManagerShouldNotBeFound("modifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllManagersByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultManagerShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the managerList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultManagerShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllManagersByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultManagerShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the managerList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultManagerShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllManagersByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where modifiedOn is not null
        defaultManagerShouldBeFound("modifiedOn.specified=true");

        // Get all the managerList where modifiedOn is null
        defaultManagerShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllManagersByModifiedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where modifiedOn greater than or equals to DEFAULT_MODIFIED_ON
        defaultManagerShouldBeFound("modifiedOn.greaterOrEqualThan=" + DEFAULT_MODIFIED_ON);

        // Get all the managerList where modifiedOn greater than or equals to UPDATED_MODIFIED_ON
        defaultManagerShouldNotBeFound("modifiedOn.greaterOrEqualThan=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllManagersByModifiedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        // Get all the managerList where modifiedOn less than or equals to DEFAULT_MODIFIED_ON
        defaultManagerShouldNotBeFound("modifiedOn.lessThan=" + DEFAULT_MODIFIED_ON);

        // Get all the managerList where modifiedOn less than or equals to UPDATED_MODIFIED_ON
        defaultManagerShouldBeFound("modifiedOn.lessThan=" + UPDATED_MODIFIED_ON);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultManagerShouldBeFound(String filter) throws Exception {
        restManagerMockMvc.perform(get("/api/managers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manager.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].managerId").value(hasItem(DEFAULT_MANAGER_ID.intValue())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));

        // Check, that the count call also returns 1
        restManagerMockMvc.perform(get("/api/managers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultManagerShouldNotBeFound(String filter) throws Exception {
        restManagerMockMvc.perform(get("/api/managers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restManagerMockMvc.perform(get("/api/managers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingManager() throws Exception {
        // Get the manager
        restManagerMockMvc.perform(get("/api/managers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManager() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        int databaseSizeBeforeUpdate = managerRepository.findAll().size();

        // Update the manager
        Manager updatedManager = managerRepository.findById(manager.getId()).get();
        // Disconnect from session so that the updates on updatedManager are not directly saved in db
        em.detach(updatedManager);
        updatedManager
            .employeeId(UPDATED_EMPLOYEE_ID)
            .managerId(UPDATED_MANAGER_ID)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedOn(UPDATED_MODIFIED_ON);
        ManagerDTO managerDTO = managerMapper.toDto(updatedManager);

        restManagerMockMvc.perform(put("/api/managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(managerDTO)))
            .andExpect(status().isOk());

        // Validate the Manager in the database
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeUpdate);
        Manager testManager = managerList.get(managerList.size() - 1);
        assertThat(testManager.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testManager.getManagerId()).isEqualTo(UPDATED_MANAGER_ID);
        assertThat(testManager.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testManager.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);

        // Validate the Manager in Elasticsearch
        verify(mockManagerSearchRepository, times(1)).save(testManager);
    }

    @Test
    @Transactional
    public void updateNonExistingManager() throws Exception {
        int databaseSizeBeforeUpdate = managerRepository.findAll().size();

        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManagerMockMvc.perform(put("/api/managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(managerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Manager in the database
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Manager in Elasticsearch
        verify(mockManagerSearchRepository, times(0)).save(manager);
    }

    @Test
    @Transactional
    public void deleteManager() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);

        int databaseSizeBeforeDelete = managerRepository.findAll().size();

        // Delete the manager
        restManagerMockMvc.perform(delete("/api/managers/{id}", manager.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Manager in Elasticsearch
        verify(mockManagerSearchRepository, times(1)).deleteById(manager.getId());
    }

    @Test
    @Transactional
    public void searchManager() throws Exception {
        // Initialize the database
        managerRepository.saveAndFlush(manager);
        when(mockManagerSearchRepository.search(queryStringQuery("id:" + manager.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(manager), PageRequest.of(0, 1), 1));
        // Search the manager
        restManagerMockMvc.perform(get("/api/_search/managers?query=id:" + manager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manager.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].managerId").value(hasItem(DEFAULT_MANAGER_ID.intValue())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manager.class);
        Manager manager1 = new Manager();
        manager1.setId(1L);
        Manager manager2 = new Manager();
        manager2.setId(manager1.getId());
        assertThat(manager1).isEqualTo(manager2);
        manager2.setId(2L);
        assertThat(manager1).isNotEqualTo(manager2);
        manager1.setId(null);
        assertThat(manager1).isNotEqualTo(manager2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManagerDTO.class);
        ManagerDTO managerDTO1 = new ManagerDTO();
        managerDTO1.setId(1L);
        ManagerDTO managerDTO2 = new ManagerDTO();
        assertThat(managerDTO1).isNotEqualTo(managerDTO2);
        managerDTO2.setId(managerDTO1.getId());
        assertThat(managerDTO1).isEqualTo(managerDTO2);
        managerDTO2.setId(2L);
        assertThat(managerDTO1).isNotEqualTo(managerDTO2);
        managerDTO1.setId(null);
        assertThat(managerDTO1).isNotEqualTo(managerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(managerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(managerMapper.fromId(null)).isNull();
    }
}
