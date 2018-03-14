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

  constructor(public http: HttpClient) {
    console.log('Hello PreparationProvider Provider');
  }

  update_preparation(poids_pilote) {
    this.poids_pilote = poids_pilote;
    console.log("appel du service");
  }

}
