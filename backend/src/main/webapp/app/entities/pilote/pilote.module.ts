import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BackendSharedModule } from '../../shared';
import {
    PiloteService,
    PilotePopupService,
    PiloteComponent,
    PiloteDetailComponent,
    PiloteDialogComponent,
    PilotePopupComponent,
    PiloteDeletePopupComponent,
    PiloteDeleteDialogComponent,
    piloteRoute,
    pilotePopupRoute,
} from './';

const ENTITY_STATES = [
    ...piloteRoute,
    ...pilotePopupRoute,
];

@NgModule({
    imports: [
        BackendSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PiloteComponent,
        PiloteDetailComponent,
        PiloteDialogComponent,
        PiloteDeleteDialogComponent,
        PilotePopupComponent,
        PiloteDeletePopupComponent,
    ],
    entryComponents: [
        PiloteComponent,
        PiloteDialogComponent,
        PilotePopupComponent,
        PiloteDeleteDialogComponent,
        PiloteDeletePopupComponent,
    ],
    providers: [
        PiloteService,
        PilotePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackendPiloteModule {}
