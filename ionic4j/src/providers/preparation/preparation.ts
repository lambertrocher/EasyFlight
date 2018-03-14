import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

/*
  Generated class for the PreparationProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class PreparationProvider {
  poids_pilote;
  poids_passager1;
  poids_passager2;
  poids_passager3;
  poids_bagages;
  airport =[];

  constructor(public http: HttpClient) {
    console.log('Hello PreparationProvider Provider');
  }

  update_preparation(poids_pilote, poids_passager1, poids_passager2, poids_passager3, poids_bagages, airport) {
    this.poids_pilote = poids_pilote;
    this.poids_passager1 = poids_passager1;
    this.poids_passager2 = poids_passager2;
    this.poids_passager3 = poids_passager3;
    this.poids_bagages = poids_bagages;
    this.airport = airport;
    console.log("aeroport", this.airport);
    console.log("appel du service");
  }

}
