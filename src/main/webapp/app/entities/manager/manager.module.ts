import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SoptorshiSharedModule } from 'app/shared';
import {
    ManagerComponent,
    ManagerDetailComponent,
    ManagerUpdateComponent,
    ManagerDeletePopupComponent,
    ManagerDeleteDialogComponent,
    managerRoute,
    managerPopupRoute
} from './';

const ENTITY_STATES = [...managerRoute, ...managerPopupRoute];

@NgModule({
    imports: [SoptorshiSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ManagerComponent,
        ManagerDetailComponent,
        ManagerUpdateComponent,
        ManagerDeleteDialogComponent,
        ManagerDeletePopupComponent
    ],
    entryComponents: [ManagerComponent, ManagerUpdateComponent, ManagerDeleteDialogComponent, ManagerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SoptorshiManagerModule {}
