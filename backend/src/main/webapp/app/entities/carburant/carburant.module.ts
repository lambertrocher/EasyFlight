import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BackendSharedModule } from '../../shared';
import {
    CarburantService,
    CarburantPopupService,
    CarburantComponent,
    CarburantDetailComponent,
    CarburantDialogComponent,
    CarburantPopupComponent,
    CarburantDeletePopupComponent,
    CarburantDeleteDialogComponent,
    carburantRoute,
    carburantPopupRoute,
} from './';

const ENTITY_STATES = [
    ...carburantRoute,
    ...carburantPopupRoute,
];

@NgModule({
    imports: [
        BackendSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CarburantComponent,
        CarburantDetailComponent,
        CarburantDialogComponent,
        CarburantDeleteDialogComponent,
        CarburantPopupComponent,
        CarburantDeletePopupComponent,
    ],
    entryComponents: [
        CarburantComponent,
        CarburantDialogComponent,
        CarburantPopupComponent,
        CarburantDeleteDialogComponent,
        CarburantDeletePopupComponent,
    ],
    providers: [
        CarburantService,
        CarburantPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackendCarburantModule {}
