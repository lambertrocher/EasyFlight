import { BaseEntity } from './../../shared';

export class Aerodrome implements BaseEntity {
    constructor(
        public id?: number,
        public oaci?: string,
        public nomAerodrome?: string,
        public controle?: boolean,
        public frequenceSol?: number,
        public frequenceTour?: number,
        public atis?: number,
        public altitude?: number,
        public piste?: BaseEntity,
    ) {
        this.controle = false;
    }
}
