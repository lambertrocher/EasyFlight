import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { BackendAvionModule } from './avion/avion.module';
import { BackendMaintenanceModule } from './maintenance/maintenance.module';
import { BackendReservoirModule } from './reservoir/reservoir.module';
import { BackendCarburantModule } from './carburant/carburant.module';
import { BackendAerodromeModule } from './aerodrome/aerodrome.module';
import { BackendPisteModule } from './piste/piste.module';
import { BackendQualificationModule } from './qualification/qualification.module';
import { BackendPiloteModule } from './pilote/pilote.module';
import { BackendVolModule } from './vol/vol.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        BackendAvionModule,
        BackendMaintenanceModule,
        BackendReservoirModule,
        BackendCarburantModule,
        BackendAerodromeModule,
        BackendPisteModule,
        BackendQualificationModule,
        BackendPiloteModule,
        BackendVolModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackendEntityModule {}
