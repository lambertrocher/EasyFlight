import { BaseEntity } from './../../shared';

export class Maintenance implements BaseEntity {
    constructor(
        public id?: number,
        public dateMaintenance?: any,
        public typeMaintenance?: string,
        public commentaire?: string,
    ) {
    }
}
