import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';

import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { SoptorshiSharedModule } from 'app/shared';
import { SoptorshiCoreModule } from 'app/core';
import { SoptorshiAppRoutingModule } from './app-routing.module';
import { SoptorshiHomeModule } from './home/home.module';
import { SoptorshiAccountModule } from './account/account.module';
import { SoptorshiEntityModule } from './entities/entity.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ErrorComponent } from './layouts';
import { MatButton, MatButtonModule, MatCheckbox, MatTab, MatTabsModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxPageScrollCoreModule } from 'ngx-page-scroll-core';
import { DeviceDetectorModule } from 'ngx-device-detector';
import { AutoCompleteModule } from 'primeng/primeng';
// import { Ng2SmartTableModule } from 'ng2-smart-table';

@NgModule({
    imports: [
        BrowserModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            alertTimeout: 5000
        }),
        SoptorshiSharedModule.forRoot(),
        SoptorshiCoreModule,
        SoptorshiHomeModule,
        SoptorshiAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        SoptorshiEntityModule,
        SoptorshiAppRoutingModule,
        BrowserAnimationsModule,
        MatButtonModule,
        MatTabsModule,
        DeviceDetectorModule.forRoot(),
        NgxPageScrollCoreModule,
        AutoCompleteModule
        // Ng2SmartTableModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class SoptorshiAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
