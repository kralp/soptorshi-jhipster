package org.soptorshi.service.impl;

import org.soptorshi.domain.Employee;
import org.soptorshi.domain.LeaveApplication;
import org.soptorshi.domain.LeaveBalance;
import org.soptorshi.domain.LeaveType;
import org.soptorshi.domain.enumeration.LeaveStatus;
import org.soptorshi.repository.EmployeeRepository;
import org.soptorshi.repository.LeaveApplicationRepository;
import org.soptorshi.repository.LeaveTypeRepository;
import org.soptorshi.service.LeaveBalanceService;
import org.soptorshi.service.dto.LeaveBalanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
@Transactional
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveApplicationRepository leaveApplicationRepository;

    private final LeaveTypeRepository leaveTypeRepository;

    private final EmployeeRepository employeeRepository;

    public LeaveBalanceServiceImpl(LeaveApplicationRepository leaveApplicationRepository, LeaveTypeRepository leaveTypeRepository, EmployeeRepository employeeRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Map<String, List<LeaveBalanceDTO>> calculateLeaveBalance(int year) {
        Map<String, List<LeaveBalanceDTO>> map = new HashMap<>();
        List<Employee> employees = employeeRepository.findAll();
        List<LeaveType> leaveTypes = getAllLeaveType();

        for (Employee employee : employees) {
            List<LeaveBalanceDTO> leaveBalanceDTOS = new ArrayList<>();
            for (LeaveType leaveType : leaveTypes) {
                leaveBalanceDTOS.add(calculate(employee.getEmployeeId(), year, leaveType));
            }
            map.put(employee.getEmployeeId(), leaveBalanceDTOS);
        }
        return map;
    }

    @Override
    public List<LeaveBalanceDTO> calculateLeaveBalance(String employeeId, int year) {
        List<LeaveBalanceDTO> leaveBalanceDTOS = new ArrayList<>();
        List<LeaveType> leaveTypes = getAllLeaveType();
        for (LeaveType leaveType : leaveTypes) {
            leaveBalanceDTOS.add(calculate(employeeId, year, leaveType));
        }
        return leaveBalanceDTOS;
    }

    @Override
    public LeaveBalanceDTO calculateLeaveBalance(String employeeId, int year, Long leaveType) {
        LeaveType type = leaveTypeRepository.getOne(leaveType);
        return calculate(employeeId, year, type);
    }

    private LeaveBalanceDTO calculate(String employeeId, int queryYear, LeaveType leaveType) {

        Optional<Employee> employee = getByEmployeeId(employeeId);

        if (employee.isPresent()) {

            LocalDate currentDate = LocalDate.now();
            LocalDate queryDateFromQueryYear = LocalDate.of(queryYear, Month.JANUARY, 1); // 2015-01-01
            LocalDate joiningDate = employee.get().getJoiningDate();

            long diffBetweenLocalDateAndJoiningDate = DAYS.between(joiningDate, currentDate);

            LocalDate lastDayOfQueryDateFromQueryYear = queryDateFromQueryYear.with(lastDayOfYear()); // 2015-12-31
            int joiningYear = joiningDate.getYear(); // 2017
            int lengthOfTheJoiningYear = currentDate.lengthOfYear(); // 365 or 366
            int joiningDayOfYear = joiningDate.getDayOfYear(); // 155
            int remainingDaysOfTheJoiningYear = lengthOfTheJoiningYear - joiningDayOfYear;

            if (queryYear < joiningYear) {
                return new LeaveBalanceDTO();
            } else {
                LeaveBalanceDTO leaveBalanceDto = new LeaveBalanceDTO();
                leaveBalanceDto.setEmployeeId(employeeId);
                List<LeaveApplication> leaveApplications = getAppliedLeave(employeeId, leaveType, queryDateFromQueryYear, lastDayOfQueryDateFromQueryYear);

                if (leaveType.getMaximumNumberOfDays() == null) { // Considering Special Leave
                    leaveBalanceDto.setTotalLeaveApplicableDays(0);
                    leaveBalanceDto.setRemainingDays(0 - leaveApplications.size());
                } else {
                    if (queryYear == joiningYear) { // joiningYear = 2017 and queryYear 2017, means he can join at any time of the year, as a result he will not get full leave
                        leaveBalanceDto.setTotalLeaveApplicableDays((remainingDaysOfTheJoiningYear * leaveType.getMaximumNumberOfDays()) / lengthOfTheJoiningYear);
                        leaveBalanceDto.setRemainingDays(
                            ((remainingDaysOfTheJoiningYear * leaveType.getMaximumNumberOfDays()) / lengthOfTheJoiningYear) - leaveApplications.size());
                    } else {
                        if (leaveType.getName().toLowerCase().contains("earned")) {
                            int averageLengthOfAYear = 365;
                            if (diffBetweenLocalDateAndJoiningDate > averageLengthOfAYear) {
                                leaveBalanceDto.setTotalLeaveApplicableDays(leaveType.getMaximumNumberOfDays());
                                leaveBalanceDto.setRemainingDays(
                                    leaveType.getMaximumNumberOfDays() - leaveApplications.size());
                            } else {
                                leaveBalanceDto.setTotalLeaveApplicableDays(0);
                                leaveBalanceDto.setRemainingDays(0 - leaveApplications.size());
                            }
                        } else {
                            leaveBalanceDto.setTotalLeaveApplicableDays(leaveType.getMaximumNumberOfDays());
                            leaveBalanceDto.setRemainingDays(
                                leaveType.getMaximumNumberOfDays() - leaveApplications.size());
                        }
                    }
                }
                leaveBalanceDto.setLeaveTypeId(leaveType.getId());
                leaveBalanceDto.setLeaveTypeName(leaveType.getName());

                return leaveBalanceDto;
            }
        }

        return new LeaveBalanceDTO();
    }


    private Optional<Employee> getByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    private List<LeaveType> getAllLeaveType() {
        return leaveTypeRepository.findAll();
    }

    private List<LeaveApplication> getAppliedLeave(final String employeeId, final LeaveType leaveType,
                                                   final LocalDate fromDate, final LocalDate toDate) {
        return leaveApplicationRepository.
            findByEmployeeIdAndLeaveTypesAndStatusAndFromDateGreaterThanAndToDateLessThan(employeeId, leaveType, LeaveStatus.ACCEPTED, fromDate, toDate);
    }
}
