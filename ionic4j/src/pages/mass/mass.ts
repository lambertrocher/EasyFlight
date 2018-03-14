import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import { App, IonicPage, NavController } from 'ionic-angular';
import { Principal } from '../../providers/auth/principal.service';
import { FirstRunPage } from '../pages';
import { LoginService } from '../../providers/login/login.service';
import { Api } from "../../providers/api/api"
import {PreparationPage} from "../preparation/preparation";
import {PreparationProvider} from "../../providers/preparation/preparation";

@IonicPage()
@Component({
  selector: 'page-mass',
  templateUrl: 'mass.html'
})
export class MassPage implements OnInit {
  @Input() preparation: PreparationPage;
  account: Account;
  public rep;
  avion;
  brasLevierTotal;


  constructor(public navCtrl: NavController,
              private principal: Principal,
              private app: App,
              private loginService: LoginService,
              private api: Api,
              private preparationProvider: PreparationProvider,
              ) {
  }


  ngOnInit() {
      this.principal.identity().then((account) => {
          if (account === null) {
              this.app.getRootNavs()[0].setRoot(FirstRunPage);
          } else {
              this.account = account;
          }
      });
      // console.log("réponse de l'api");
      // this.api.get("avions").subscribe(response => {
      //     console.log(response[0].id);
      //     this.rep = response[0].id;
      // });
      // console.log(this.rep);
      this.masse_et_centrage();
  }

  isAuthenticated() {
    return this.principal.isAuthenticated();
  }

  logout() {
    this.loginService.logout();
    this.app.getRootNavs()[0].setRoot(FirstRunPage);
  }

  mass() {
    this.navCtrl.push('MassPage');
  }

  masse_et_centrage(){
    let levierBagages;
    let levierAvant;
    let levierArriere;
    let levierReservoir;
    let poidsPilote;
    let poidsPassager1;
    let poidsPassager2;
    let poidsPassager3;
    let poidsBagages;
    let momentTotal;
    let poidsTotal;
    let masseVideAvion;
      this.api.get("avions/3").subscribe(response => {
          this.avion = response;
          levierBagages = this.avion.levierBagages;
          levierAvant = this.avion.levierPassagersAvant;
          levierArriere = this.avion.levierPassagersArriere;
          levierReservoir = this.avion.reservoir.levier;
          masseVideAvion = this.avion.masseVideAvion;
          poidsPilote = this.preparationProvider.poids_pilote;
          poidsPassager1 = this.preparationProvider.poids_passager1;
          poidsPassager2 = this.preparationProvider.poids_passager2;
          poidsPassager3 = this.preparationProvider.poids_passager3;
          poidsBagages = this.preparationProvider.poids_bagages;
          console.log("poids bages", poidsBagages);

          momentTotal = 0.3*masseVideAvion + (poidsPilote + poidsPassager1)*levierAvant + (poidsPassager2 + poidsPassager3)*levierArriere + poidsBagages*levierBagages;
          poidsTotal = masseVideAvion + poidsPilote + poidsPassager1 + poidsPassager2 + poidsPassager3 + poidsBagages;
          console.log("moment total", momentTotal);
          console.log("poids total", poidsTotal);
          this.brasLevierTotal = momentTotal / poidsTotal;
          console.log(this.brasLevierTotal);
      });
  }

  ionViewWillEnter(){
      this.masse_et_centrage();
  }


}
