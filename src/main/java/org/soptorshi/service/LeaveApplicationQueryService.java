package org.soptorshi.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.soptorshi.domain.LeaveApplication;
import org.soptorshi.domain.*; // for static metamodels
import org.soptorshi.repository.LeaveApplicationRepository;
import org.soptorshi.repository.search.LeaveApplicationSearchRepository;
import org.soptorshi.service.dto.LeaveApplicationCriteria;
import org.soptorshi.service.dto.LeaveApplicationDTO;
import org.soptorshi.service.mapper.LeaveApplicationMapper;

/**
 * Service for executing complex queries for LeaveApplication entities in the database.
 * The main input is a {@link LeaveApplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveApplicationDTO} or a {@link Page} of {@link LeaveApplicationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveApplicationQueryService extends QueryService<LeaveApplication> {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationQueryService.class);

    private final LeaveApplicationRepository leaveApplicationRepository;

    private final LeaveApplicationMapper leaveApplicationMapper;

    private final LeaveApplicationSearchRepository leaveApplicationSearchRepository;

    public LeaveApplicationQueryService(LeaveApplicationRepository leaveApplicationRepository, LeaveApplicationMapper leaveApplicationMapper, LeaveApplicationSearchRepository leaveApplicationSearchRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
        this.leaveApplicationSearchRepository = leaveApplicationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaveApplicationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveApplicationDTO> findByCriteria(LeaveApplicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationMapper.toDto(leaveApplicationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaveApplicationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveApplicationDTO> findByCriteria(LeaveApplicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationRepository.findAll(specification, page)
            .map(leaveApplicationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaveApplicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationRepository.count(specification);
    }

    /**
     * Function to convert LeaveApplicationCriteria to a {@link Specification}
     */
    private Specification<LeaveApplication> createSpecification(LeaveApplicationCriteria criteria) {
        Specification<LeaveApplication> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), LeaveApplication_.id));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmployeeId(), LeaveApplication_.employeeId));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), LeaveApplication_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), LeaveApplication_.toDate));
            }
            if (criteria.getNumberOfDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfDays(), LeaveApplication_.numberOfDays));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), LeaveApplication_.reason));
            }
            if (criteria.getAppliedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppliedBy(), LeaveApplication_.appliedBy));
            }
            if (criteria.getAppliedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAppliedOn(), LeaveApplication_.appliedOn));
            }
            if (criteria.getActionTakenBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActionTakenBy(), LeaveApplication_.actionTakenBy));
            }
            if (criteria.getActionTakenOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActionTakenOn(), LeaveApplication_.actionTakenOn));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), LeaveApplication_.status));
            }
            if (criteria.getLeaveTypesId() != null) {
                specification = specification.and(buildSpecification(criteria.getLeaveTypesId(),
                    root -> root.join(LeaveApplication_.leaveTypes, JoinType.LEFT).get(LeaveType_.id)));
            }
        }
        return specification;
    }
}
