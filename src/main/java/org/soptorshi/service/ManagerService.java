package org.soptorshi.service;

import org.soptorshi.domain.Manager;
import org.soptorshi.repository.ManagerRepository;
import org.soptorshi.repository.search.ManagerSearchRepository;
import org.soptorshi.service.dto.ManagerDTO;
import org.soptorshi.service.mapper.ManagerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Manager.
 */
@Service
@Transactional
public class ManagerService {

    private final Logger log = LoggerFactory.getLogger(ManagerService.class);

    private final ManagerRepository managerRepository;

    private final ManagerMapper managerMapper;

    private final ManagerSearchRepository managerSearchRepository;

    public ManagerService(ManagerRepository managerRepository, ManagerMapper managerMapper, ManagerSearchRepository managerSearchRepository) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
        this.managerSearchRepository = managerSearchRepository;
    }

    /**
     * Save a manager.
     *
     * @param managerDTO the entity to save
     * @return the persisted entity
     */
    public ManagerDTO save(ManagerDTO managerDTO) {
        log.debug("Request to save Manager : {}", managerDTO);
        Manager manager = managerMapper.toEntity(managerDTO);
        manager = managerRepository.save(manager);
        ManagerDTO result = managerMapper.toDto(manager);
        managerSearchRepository.save(manager);
        return result;
    }

    /**
     * Get all the managers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ManagerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Managers");
        return managerRepository.findAll(pageable)
            .map(managerMapper::toDto);
    }


    /**
     * Get one manager by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ManagerDTO> findOne(Long id) {
        log.debug("Request to get Manager : {}", id);
        return managerRepository.findById(id)
            .map(managerMapper::toDto);
    }

    /**
     * Delete the manager by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Manager : {}", id);
        managerRepository.deleteById(id);
        managerSearchRepository.deleteById(id);
    }

    /**
     * Search for the manager corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ManagerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Managers for query {}", query);
        return managerSearchRepository.search(queryStringQuery(query), pageable)
            .map(managerMapper::toDto);
    }
}
