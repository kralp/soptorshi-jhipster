import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatTabsModule } from '@angular/material';

@NgModule({
    imports: [
        MatTabsModule,
        RouterModule.forChild([
            {
                path: 'department',
                loadChildren: './department/department.module#SoptorshiDepartmentModule'
            },
            {
                path: 'designation',
                loadChildren: './designation/designation.module#SoptorshiDesignationModule'
            },
            {
                path: 'employee',
                loadChildren: './employee/employee.module#SoptorshiEmployeeModule'
            },
            {
                path: 'attachment',
                loadChildren: './attachment/attachment.module#SoptorshiAttachmentModule'
            },
            {
                path: 'academic-information',
                loadChildren: './academic-information/academic-information.module#SoptorshiAcademicInformationModule'
            },
            {
                path: 'training-information',
                loadChildren: './training-information/training-information.module#SoptorshiTrainingInformationModule'
            },
            {
                path: 'family-information',
                loadChildren: './family-information/family-information.module#SoptorshiFamilyInformationModule'
            },
            {
                path: 'reference-information',
                loadChildren: './reference-information/reference-information.module#SoptorshiReferenceInformationModule'
            },
            {
                path: 'experience-information',
                loadChildren: './experience-information/experience-information.module#SoptorshiExperienceInformationModule'
            },
            {
                path: 'department',
                loadChildren: './department/department.module#SoptorshiDepartmentModule'
            },
            {
                path: 'designation',
                loadChildren: './designation/designation.module#SoptorshiDesignationModule'
            },
            {
                path: 'attachment',
                loadChildren: './attachment/attachment.module#SoptorshiAttachmentModule'
            },

            {
                path: 'training-information',
                loadChildren: './training-information/training-information.module#SoptorshiTrainingInformationModule'
            },
            {
                path: 'family-information',
                loadChildren: './family-information/family-information.module#SoptorshiFamilyInformationModule'
            },
            {
                path: 'reference-information',
                loadChildren: './reference-information/reference-information.module#SoptorshiReferenceInformationModule'
            },
            {
                path: 'experience-information',
                loadChildren: './experience-information/experience-information.module#SoptorshiExperienceInformationModule'
            },
            {
                path: 'attachment',
                loadChildren: './attachment/attachment.module#SoptorshiAttachmentModule'
            },
            {
                path: 'academic-information-attachment',
                loadChildren:
                    './academic-information-attachment/academic-information-attachment.module#SoptorshiAcademicInformationAttachmentModule'
            },
            {
                path: 'experience-information-attachment',
                loadChildren:
                    './experience-information-attachment/experience-information-attachment.module#SoptorshiExperienceInformationAttachmentModule'
            },
            {
                path: 'training-information-attachment',
                loadChildren:
                    './training-information-attachment/training-information-attachment.module#SoptorshiTrainingInformationAttachmentModule'
            },
            {
                path: 'employee',
                loadChildren: './employee/employee.module#SoptorshiEmployeeModule'
            },

            {
                path: 'training-information',
                loadChildren: './training-information/training-information.module#SoptorshiTrainingInformationModule'
            },
            {
                path: 'experience-information',
                loadChildren: './experience-information/experience-information.module#SoptorshiExperienceInformationModule'
            },
            {
                path: 'employee',
                loadChildren: './employee/employee.module#SoptorshiEmployeeModule'
            },
            {
                path: 'office',
                loadChildren: './office/office.module#SoptorshiOfficeModule'
            },
            {
                path: 'financial-account-year',
                loadChildren: './financial-account-year/financial-account-year.module#SoptorshiFinancialAccountYearModule'
            },
            {
                path: 'tax',
                loadChildren: './tax/tax.module#SoptorshiTaxModule'
            },
            {
                path: 'salary',
                loadChildren: './salary/salary.module#SoptorshiSalaryModule'
            },
            {
                path: 'provident-fund',
                loadChildren: './provident-fund/provident-fund.module#SoptorshiProvidentFundModule'
            },
            {
                path: 'monthly-salary',
                loadChildren: './monthly-salary/monthly-salary.module#SoptorshiMonthlySalaryModule'
            },
            {
                path: 'advance',
                loadChildren: './advance/advance.module#SoptorshiAdvanceModule'
            },
            {
                path: 'fine',
                loadChildren: './fine/fine.module#SoptorshiFineModule'
            },
            {
                path: 'loan',
                loadChildren: './loan/loan.module#SoptorshiLoanModule'
            },
            {
                path: 'holiday-type',
                loadChildren: './holiday-type/holiday-type.module#SoptorshiHolidayTypeModule'
            },
            {
                path: 'holiday',
                loadChildren: './holiday/holiday.module#SoptorshiHolidayModule'
            },
            {
                path: 'employee',
                loadChildren: './employee/employee.module#SoptorshiEmployeeModule'
            },
            {
                path: 'tax',
                loadChildren: './tax/tax.module#SoptorshiTaxModule'
            },
            {
                path: 'salary',
                loadChildren: './salary/salary.module#SoptorshiSalaryModule'
            },
            {
                path: 'monthly-salary',
                loadChildren: './monthly-salary/monthly-salary.module#SoptorshiMonthlySalaryModule'
            },
            {
                path: 'payroll-management',
                loadChildren: './payroll-management/payroll-management.module#SoptorshiPayrollManagementModule'
            },
            {
                path: 'tax',
                loadChildren: './tax/tax.module#SoptorshiTaxModule'
            },
            {
                path: 'designation-wise-allowance',
                loadChildren: './designation-wise-allowance/designation-wise-allowance.module#SoptorshiDesignationWiseAllowanceModule'
            },
            {
                path: 'provident-fund',
                loadChildren: './provident-fund/provident-fund.module#SoptorshiProvidentFundModule'
            },
            {
                path: 'monthly-salary',
                loadChildren: './monthly-salary/monthly-salary.module#SoptorshiMonthlySalaryModule'
            },
            {
                path: 'advance',
                loadChildren: './advance/advance.module#SoptorshiAdvanceModule'
            },
            {
                path: 'fine',
                loadChildren: './fine/fine.module#SoptorshiFineModule'
            },
            {
                path: 'loan',
                loadChildren: './loan/loan.module#SoptorshiLoanModule'
            },
            {
                path: 'fine-management',
                loadChildren: './fine-management/fine-management.module#SoptorshiFineManagementModule'
            },
            {
                path: 'advance-management',
                loadChildren: './advance-management/advance-management.module#SoptorshiAdvanceManagementModule'
            },
            {
                path: 'loan-management',
                loadChildren: './loan-management/loan-management.module#SoptorshiLoanManagementModule'
            },
            {
                path: 'provident-management',
                loadChildren: './provident-management/provident-management.module#SoptorshiProvidentManagementModule'
            },
            {
                path: 'allowance-management',
                loadChildren: './allowance-management/allowance-management.module#SoptorshiAllowanceManagementModule'
            },
            {
                path: 'salary',
                loadChildren: './salary/salary.module#SoptorshiSalaryModule'
            },
            {
                path: 'fine',
                loadChildren: './fine/fine.module#SoptorshiFineModule'
            },
            {
                path: 'fine',
                loadChildren: './fine/fine.module#SoptorshiFineModule'
            },
            {
                path: 'advance',
                loadChildren: './advance/advance.module#SoptorshiAdvanceModule'
            },
            {
                path: 'fine',
                loadChildren: './fine/fine.module#SoptorshiFineModule'
            },
            {
                path: 'loan',
                loadChildren: './loan/loan.module#SoptorshiLoanModule'
            },
            {
                path: 'fine-advance-loan-management',
                loadChildren: './fine-advance-loan-management/fine-advance-loan-management.module#SoptorshiFineAdvanceLoanManagementModule'
            },
            {
                path: 'employee',
                loadChildren: './employee/employee.module#SoptorshiEmployeeModule'
            },
            {
                path: 'manager',
                loadChildren: './manager/manager.module#SoptorshiManagerModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SoptorshiEntityModule {}
