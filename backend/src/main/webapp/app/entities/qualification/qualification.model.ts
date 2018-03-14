import { BaseEntity } from './../../shared';

export class Qualification implements BaseEntity {
    constructor(
        public id?: number,
        public expiration?: any,
        public typeQualification?: string,
    ) {
    }
}
