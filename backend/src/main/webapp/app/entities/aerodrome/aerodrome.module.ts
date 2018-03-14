import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BackendSharedModule } from '../../shared';
import {
    AerodromeService,
    AerodromePopupService,
    AerodromeComponent,
    AerodromeDetailComponent,
    AerodromeDialogComponent,
    AerodromePopupComponent,
    AerodromeDeletePopupComponent,
    AerodromeDeleteDialogComponent,
    aerodromeRoute,
    aerodromePopupRoute,
} from './';

const ENTITY_STATES = [
    ...aerodromeRoute,
    ...aerodromePopupRoute,
];

@NgModule({
    imports: [
        BackendSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AerodromeComponent,
        AerodromeDetailComponent,
        AerodromeDialogComponent,
        AerodromeDeleteDialogComponent,
        AerodromePopupComponent,
        AerodromeDeletePopupComponent,
    ],
    entryComponents: [
        AerodromeComponent,
        AerodromeDialogComponent,
        AerodromePopupComponent,
        AerodromeDeleteDialogComponent,
        AerodromeDeletePopupComponent,
    ],
    providers: [
        AerodromeService,
        AerodromePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackendAerodromeModule {}
