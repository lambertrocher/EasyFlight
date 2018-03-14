import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BackendSharedModule } from '../../shared';
import {
    VolService,
    VolPopupService,
    VolComponent,
    VolDetailComponent,
    VolDialogComponent,
    VolPopupComponent,
    VolDeletePopupComponent,
    VolDeleteDialogComponent,
    volRoute,
    volPopupRoute,
} from './';

const ENTITY_STATES = [
    ...volRoute,
    ...volPopupRoute,
];

@NgModule({
    imports: [
        BackendSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        VolComponent,
        VolDetailComponent,
        VolDialogComponent,
        VolDeleteDialogComponent,
        VolPopupComponent,
        VolDeletePopupComponent,
    ],
    entryComponents: [
        VolComponent,
        VolDialogComponent,
        VolPopupComponent,
        VolDeleteDialogComponent,
        VolDeletePopupComponent,
    ],
    providers: [
        VolService,
        VolPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackendVolModule {}
