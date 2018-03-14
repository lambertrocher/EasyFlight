import { BaseEntity } from './../../shared';

export class Pilote implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public prenom?: string,
        public numeroTel?: number,
        public numeroLicence?: string,
        public dateValiditeLicence?: any,
        public adresseMail?: string,
        public certificatMedical?: string,
        public qualification?: BaseEntity,
    ) {
    }
}
