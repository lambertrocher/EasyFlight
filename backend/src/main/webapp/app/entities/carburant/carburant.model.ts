import { BaseEntity } from './../../shared';

export class Carburant implements BaseEntity {
    constructor(
        public id?: number,
        public typeCarburant?: string,
        public poidsParLitre?: number,
    ) {
    }
}
