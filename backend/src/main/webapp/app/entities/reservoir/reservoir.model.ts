import { BaseEntity } from './../../shared';

export class Reservoir implements BaseEntity {
    constructor(
        public id?: number,
        public quantiteMax?: number,
        public quantitePresente?: number,
        public capaciteMaxUtile?: number,
        public levier?: number,
        public carburant?: BaseEntity,
    ) {
    }
}
