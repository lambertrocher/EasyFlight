import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BackendSharedModule } from '../../shared';
import {
    AvionService,
    AvionPopupService,
    AvionComponent,
    AvionDetailComponent,
    AvionDialogComponent,
    AvionPopupComponent,
    AvionDeletePopupComponent,
    AvionDeleteDialogComponent,
    avionRoute,
    avionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...avionRoute,
    ...avionPopupRoute,
];

@NgModule({
    imports: [
        BackendSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AvionComponent,
        AvionDetailComponent,
        AvionDialogComponent,
        AvionDeleteDialogComponent,
        AvionPopupComponent,
        AvionDeletePopupComponent,
    ],
    entryComponents: [
        AvionComponent,
        AvionDialogComponent,
        AvionPopupComponent,
        AvionDeleteDialogComponent,
        AvionDeletePopupComponent,
    ],
    providers: [
        AvionService,
        AvionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackendAvionModule {}
