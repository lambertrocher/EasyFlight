import { BaseEntity } from './../../shared';

export class Vol implements BaseEntity {
    constructor(
        public id?: number,
        public dateVol?: any,
        public avion?: BaseEntity,
        public pilote?: BaseEntity,
        public aerodrome?: BaseEntity,
    ) {
    }
}
