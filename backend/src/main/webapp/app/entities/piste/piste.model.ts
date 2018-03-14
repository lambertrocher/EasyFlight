import { BaseEntity } from './../../shared';

export class Piste implements BaseEntity {
    constructor(
        public id?: number,
        public longueur?: number,
        public typeTerrain?: string,
    ) {
    }
}
