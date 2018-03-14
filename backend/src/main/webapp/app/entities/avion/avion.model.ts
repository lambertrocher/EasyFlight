import { BaseEntity } from './../../shared';

export class Avion implements BaseEntity {
    constructor(
        public id?: number,
        public immatriculation?: string,
        public typeAvion?: string,
        public nbSiegesAvant?: number,
        public nbSiegesArriere?: number,
        public masseVideAvion?: number,
        public masseMaxBagages?: number,
        public masseMaxTotale?: number,
        public typeMoteur?: string,
        public puissanceAvion?: number,
        public vitesseMax?: number,
        public vitesseCroisiere?: number,
        public facteurBase?: number,
        public nbHeuresVol?: number,
        public levierPassagersAvant?: number,
        public levierPassagersArriere?: number,
        public levierBagages?: number,
        public reservoir?: BaseEntity,
        public maintenance?: BaseEntity,
    ) {
    }
}
