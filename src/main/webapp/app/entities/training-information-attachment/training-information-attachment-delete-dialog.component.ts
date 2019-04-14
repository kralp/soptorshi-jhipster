import { Component, OnInit, OnDestroy, EventEmitter, Input, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITrainingInformationAttachment } from 'app/shared/model/training-information-attachment.model';
import { TrainingInformationAttachmentService } from './training-information-attachment.service';

@Component({
    selector: 'jhi-training-information-attachment-delete-dialog',
    templateUrl: './training-information-attachment-delete-dialog.component.html'
})
export class TrainingInformationAttachmentDeleteDialogComponent {
    trainingInformationAttachment: ITrainingInformationAttachment;

    constructor(
        protected trainingInformationAttachmentService: TrainingInformationAttachmentService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.trainingInformationAttachmentService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'trainingInformationAttachmentListModification',
                content: 'Deleted an trainingInformationAttachment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-training-information-attachment-delete-popup',
    template: ''
})
export class TrainingInformationAttachmentDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;
    @Input()
    trainingInformationAttachment: ITrainingInformationAttachment;
    @Output()
    showTrainingInformationAttachmentSection: EventEmitter<any> = new EventEmitter();

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        setTimeout(() => {
            this.ngbModalRef = this.modalService.open(TrainingInformationAttachmentDeleteDialogComponent as Component, {
                size: 'lg',
                backdrop: 'static'
            });
            this.ngbModalRef.componentInstance.trainingInformationAttachment = this.trainingInformationAttachment;
            this.ngbModalRef.result.then(
                result => {
                    this.showTrainingInformationAttachmentSection.emit();
                    this.ngbModalRef = null;
                },
                reason => {
                    this.showTrainingInformationAttachmentSection.emit();
                    this.ngbModalRef = null;
                }
            );
        }, 0);
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
