import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BackendSharedModule } from '../../shared';
import {
    PisteService,
    PistePopupService,
    PisteComponent,
    PisteDetailComponent,
    PisteDialogComponent,
    PistePopupComponent,
    PisteDeletePopupComponent,
    PisteDeleteDialogComponent,
    pisteRoute,
    pistePopupRoute,
} from './';

const ENTITY_STATES = [
    ...pisteRoute,
    ...pistePopupRoute,
];

@NgModule({
    imports: [
        BackendSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PisteComponent,
        PisteDetailComponent,
        PisteDialogComponent,
        PisteDeleteDialogComponent,
        PistePopupComponent,
        PisteDeletePopupComponent,
    ],
    entryComponents: [
        PisteComponent,
        PisteDialogComponent,
        PistePopupComponent,
        PisteDeleteDialogComponent,
        PisteDeletePopupComponent,
    ],
    providers: [
        PisteService,
        PistePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackendPisteModule {}
