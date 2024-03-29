package org.soptorshi.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.soptorshi.domain.enumeration.MonthType;

/**
 * A MonthlySalary.
 */
@Entity
@Table(name = "monthly_salary")
@Document(indexName = "monthlysalary")
public class MonthlySalary implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_year", nullable = false)
    private Integer year;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private MonthType month;

    @NotNull
    @Column(name = "basic", precision = 10, scale = 2, nullable = false)
    private BigDecimal basic;

    @Column(name = "house_rent", precision = 10, scale = 2)
    private BigDecimal houseRent;

    @Column(name = "medical_allowance", precision = 10, scale = 2)
    private BigDecimal medicalAllowance;

    @Column(name = "other_allowance", precision = 10, scale = 2)
    private BigDecimal otherAllowance;

    @Column(name = "absent")
    private Integer absent;

    @Column(name = "fine", precision = 10, scale = 2)
    private BigDecimal fine;

    @Column(name = "advance_ho", precision = 10, scale = 2)
    private BigDecimal advanceHO;

    @Column(name = "advance_factory", precision = 10, scale = 2)
    private BigDecimal advanceFactory;

    @Column(name = "provident_fund", precision = 10, scale = 2)
    private BigDecimal providentFund;

    @Column(name = "tax", precision = 10, scale = 2)
    private BigDecimal tax;

    @Column(name = "loan_amount", precision = 10, scale = 2)
    private BigDecimal loanAmount;

    @Column(name = "payable", precision = 10, scale = 2)
    private BigDecimal payable;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_on")
    private LocalDate modifiedOn;

    @ManyToOne
    @JsonIgnoreProperties("monthlySalaries")
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public MonthlySalary year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public MonthType getMonth() {
        return month;
    }

    public MonthlySalary month(MonthType month) {
        this.month = month;
        return this;
    }

    public void setMonth(MonthType month) {
        this.month = month;
    }

    public BigDecimal getBasic() {
        return basic;
    }

    public MonthlySalary basic(BigDecimal basic) {
        this.basic = basic;
        return this;
    }

    public void setBasic(BigDecimal basic) {
        this.basic = basic;
    }

    public BigDecimal getHouseRent() {
        return houseRent;
    }

    public MonthlySalary houseRent(BigDecimal houseRent) {
        this.houseRent = houseRent;
        return this;
    }

    public void setHouseRent(BigDecimal houseRent) {
        this.houseRent = houseRent;
    }

    public BigDecimal getMedicalAllowance() {
        return medicalAllowance;
    }

    public MonthlySalary medicalAllowance(BigDecimal medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
        return this;
    }

    public void setMedicalAllowance(BigDecimal medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
    }

    public BigDecimal getOtherAllowance() {
        return otherAllowance;
    }

    public MonthlySalary otherAllowance(BigDecimal otherAllowance) {
        this.otherAllowance = otherAllowance;
        return this;
    }

    public void setOtherAllowance(BigDecimal otherAllowance) {
        this.otherAllowance = otherAllowance;
    }

    public Integer getAbsent() {
        return absent;
    }

    public MonthlySalary absent(Integer absent) {
        this.absent = absent;
        return this;
    }

    public void setAbsent(Integer absent) {
        this.absent = absent;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public MonthlySalary fine(BigDecimal fine) {
        this.fine = fine;
        return this;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }

    public BigDecimal getAdvanceHO() {
        return advanceHO;
    }

    public MonthlySalary advanceHO(BigDecimal advanceHO) {
        this.advanceHO = advanceHO;
        return this;
    }

    public void setAdvanceHO(BigDecimal advanceHO) {
        this.advanceHO = advanceHO;
    }

    public BigDecimal getAdvanceFactory() {
        return advanceFactory;
    }

    public MonthlySalary advanceFactory(BigDecimal advanceFactory) {
        this.advanceFactory = advanceFactory;
        return this;
    }

    public void setAdvanceFactory(BigDecimal advanceFactory) {
        this.advanceFactory = advanceFactory;
    }

    public BigDecimal getProvidentFund() {
        return providentFund;
    }

    public MonthlySalary providentFund(BigDecimal providentFund) {
        this.providentFund = providentFund;
        return this;
    }

    public void setProvidentFund(BigDecimal providentFund) {
        this.providentFund = providentFund;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public MonthlySalary tax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public MonthlySalary loanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
        return this;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getPayable() {
        return payable;
    }

    public MonthlySalary payable(BigDecimal payable) {
        this.payable = payable;
        return this;
    }

    public void setPayable(BigDecimal payable) {
        this.payable = payable;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public MonthlySalary modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDate getModifiedOn() {
        return modifiedOn;
    }

    public MonthlySalary modifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public void setModifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Employee getEmployee() {
        return employee;
    }

    public MonthlySalary employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MonthlySalary monthlySalary = (MonthlySalary) o;
        if (monthlySalary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), monthlySalary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MonthlySalary{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", month='" + getMonth() + "'" +
            ", basic=" + getBasic() +
            ", houseRent=" + getHouseRent() +
            ", medicalAllowance=" + getMedicalAllowance() +
            ", otherAllowance=" + getOtherAllowance() +
            ", absent=" + getAbsent() +
            ", fine=" + getFine() +
            ", advanceHO=" + getAdvanceHO() +
            ", advanceFactory=" + getAdvanceFactory() +
            ", providentFund=" + getProvidentFund() +
            ", tax=" + getTax() +
            ", loanAmount=" + getLoanAmount() +
            ", payable=" + getPayable() +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            "}";
    }
}
