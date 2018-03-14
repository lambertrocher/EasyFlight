import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BackendSharedModule } from '../../shared';
import {
    ReservoirService,
    ReservoirPopupService,
    ReservoirComponent,
    ReservoirDetailComponent,
    ReservoirDialogComponent,
    ReservoirPopupComponent,
    ReservoirDeletePopupComponent,
    ReservoirDeleteDialogComponent,
    reservoirRoute,
    reservoirPopupRoute,
} from './';

const ENTITY_STATES = [
    ...reservoirRoute,
    ...reservoirPopupRoute,
];

@NgModule({
    imports: [
        BackendSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ReservoirComponent,
        ReservoirDetailComponent,
        ReservoirDialogComponent,
        ReservoirDeleteDialogComponent,
        ReservoirPopupComponent,
        ReservoirDeletePopupComponent,
    ],
    entryComponents: [
        ReservoirComponent,
        ReservoirDialogComponent,
        ReservoirPopupComponent,
        ReservoirDeleteDialogComponent,
        ReservoirDeletePopupComponent,
    ],
    providers: [
        ReservoirService,
        ReservoirPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackendReservoirModule {}
