import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ILeaveAttachment } from 'app/shared/model/leave-attachment.model';
import { LeaveAttachmentService } from './leave-attachment.service';
import { ILeaveApplication } from 'app/shared/model/leave-application.model';
import { LeaveApplicationService } from 'app/entities/leave-application';

@Component({
    selector: 'jhi-leave-attachment-update',
    templateUrl: './leave-attachment-update.component.html'
})
export class LeaveAttachmentUpdateComponent implements OnInit {
    leaveAttachment: ILeaveAttachment;
    isSaving: boolean;

    leaveapplications: ILeaveApplication[];

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected leaveAttachmentService: LeaveAttachmentService,
        protected leaveApplicationService: LeaveApplicationService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ leaveAttachment }) => {
            this.leaveAttachment = leaveAttachment;
        });
        this.leaveApplicationService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ILeaveApplication[]>) => mayBeOk.ok),
                map((response: HttpResponse<ILeaveApplication[]>) => response.body)
            )
            .subscribe((res: ILeaveApplication[]) => (this.leaveapplications = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        /*window.history.back();*/
        this.router.navigate(['/leave-application']);
    }

    save() {
        this.isSaving = true;
        if (this.leaveAttachment.id !== undefined) {
            this.subscribeToSaveResponse(this.leaveAttachmentService.update(this.leaveAttachment));
        } else {
            this.subscribeToSaveResponse(this.leaveAttachmentService.create(this.leaveAttachment));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveAttachment>>) {
        result.subscribe((res: HttpResponse<ILeaveAttachment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackLeaveApplicationById(index: number, item: ILeaveApplication) {
        return item.id;
    }
}
