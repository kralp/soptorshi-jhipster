import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { LeaveApplication } from 'app/shared/model/leave-application.model';
import { LeaveApplicationService } from './leave-application.service';
import { LeaveApplicationComponent } from './leave-application.component';
import { LeaveApplicationDetailComponent } from './leave-application-detail.component';
import { LeaveApplicationUpdateComponent } from './leave-application-update.component';
import { LeaveApplicationDeletePopupComponent } from './leave-application-delete-dialog.component';
import { ILeaveApplication } from 'app/shared/model/leave-application.model';
import { OthersLeaveApplicationComponent } from 'app/entities/leave-application/others-leave-application.component';
import { ReviewLeaveApplicationComponent } from 'app/entities/leave-application/review-leave-application.component';
import { LeaveBalanceComponent } from 'app/entities/leave-balance/leave-balance.component';
import { OthersLeaveApplicationHistoryComponent } from 'app/entities/leave-application/others-leave-application-history.component';

@Injectable({ providedIn: 'root' })
export class LeaveApplicationResolve implements Resolve<ILeaveApplication> {
    constructor(private service: LeaveApplicationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILeaveApplication> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<LeaveApplication>) => response.ok),
                map((leaveApplication: HttpResponse<LeaveApplication>) => leaveApplication.body)
            );
        }
        return of(new LeaveApplication());
    }
}

export const leaveApplicationRoute: Routes = [
    {
        path: '',
        component: LeaveApplicationComponent,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_LEAVE_APPLICATION'],
            pageTitle: 'LeaveApplications'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: LeaveApplicationDetailComponent,
        resolve: {
            leaveApplication: LeaveApplicationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_LEAVE_APPLICATION'],
            pageTitle: 'LeaveApplications'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: LeaveApplicationUpdateComponent,
        resolve: {
            leaveApplication: LeaveApplicationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_LEAVE_APPLICATION'],
            pageTitle: 'LeaveApplications'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/others',
        component: OthersLeaveApplicationComponent,
        resolve: {
            leaveApplication: LeaveApplicationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_OTHERS_LEAVE_APPLICATION'],
            pageTitle: 'LeaveApplications'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: LeaveApplicationUpdateComponent,
        resolve: {
            leaveApplication: LeaveApplicationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_LEAVE_APPLICATION'],
            pageTitle: 'LeaveApplications'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'review',
        component: ReviewLeaveApplicationComponent,
        resolve: {
            leaveApplication: LeaveApplicationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_LEAVE_APPROVAL'],
            pageTitle: 'ReviewLeaveApplication'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'history/others',
        component: OthersLeaveApplicationHistoryComponent,
        resolve: {
            leaveApplication: LeaveApplicationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_OTHERS_LEAVE_APPLICATION'],
            pageTitle: 'LeaveHistory'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const leaveApplicationPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: LeaveApplicationDeletePopupComponent,
        resolve: {
            leaveApplication: LeaveApplicationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_LEAVE_APPLICATION'],
            pageTitle: 'LeaveApplications'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
