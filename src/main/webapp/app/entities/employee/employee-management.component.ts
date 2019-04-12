import { AfterContentInit, Component, OnInit, ViewChild } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';
import { IEmployee } from 'app/shared/model/employee.model';
import { NgbTab, NgbTabset } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeService } from 'app/entities/employee/employee.service';
import { Table } from 'primeng/table';

@Component({
    selector: 'jhi-employee-management',
    templateUrl: './employee-management.component.html',
    styles: []
})
export class EmployeeManagementComponent implements OnInit, AfterContentInit {
    employee: IEmployee;
    activeTabId: string;
    editable: boolean;
    showEditOrCancelButton: boolean;
    @ViewChild('#tabSet') tabSet: NgbTabset;
    constructor(
        protected jhiAlertService: JhiAlertService,
        protected activatedRoute: ActivatedRoute,
        protected employeeService: EmployeeService
    ) {}

    ngOnInit() {
        this.showEditOrCancelButton = true;
        this.activatedRoute.data.subscribe(({ employee }) => {
            this.employee = employee;
        });

        this.activeTabId = this.employeeService.selectedTabId;
    }

    ngAfterContentInit(): void {
        if (this.employee.id === undefined) this.editable = true;
        else this.editable = false;
    }

    edit(): void {
        this.editable = true;
    }

    cancelEdit(): void {
        if (this.employee.id === undefined) this.previousState();
        else this.editable = false;
    }

    tabSelected($event: any) {
        this.employeeService.selectedTabId = $event.nextId;
        if (this.employeeService.selectedTabId === 'personalInformation') {
            this.showEditOrCancelButton = true;
        } else {
            this.showEditOrCancelButton = false;
        }
    }

    previousState() {
        window.history.back();
    }
}
