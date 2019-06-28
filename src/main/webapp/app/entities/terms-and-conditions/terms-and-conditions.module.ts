import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SoptorshiSharedModule } from 'app/shared';
import {
    TermsAndConditionsComponent,
    TermsAndConditionsDetailComponent,
    TermsAndConditionsUpdateComponent,
    TermsAndConditionsDeletePopupComponent,
    TermsAndConditionsDeleteDialogComponent,
    termsAndConditionsRoute,
    termsAndConditionsPopupRoute
} from './';

const ENTITY_STATES = [...termsAndConditionsRoute, ...termsAndConditionsPopupRoute];

@NgModule({
    imports: [SoptorshiSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TermsAndConditionsComponent,
        TermsAndConditionsDetailComponent,
        TermsAndConditionsUpdateComponent,
        TermsAndConditionsDeleteDialogComponent,
        TermsAndConditionsDeletePopupComponent
    ],
    entryComponents: [
        TermsAndConditionsComponent,
        TermsAndConditionsUpdateComponent,
        TermsAndConditionsDeleteDialogComponent,
        TermsAndConditionsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SoptorshiTermsAndConditionsModule {}
