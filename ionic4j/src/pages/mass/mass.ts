import { Component, OnInit } from '@angular/core';
import { App, IonicPage, NavController } from 'ionic-angular';
import { Principal } from '../../providers/auth/principal.service';
import { FirstRunPage } from '../pages';
import { LoginService } from '../../providers/login/login.service';
import { Api } from "../../providers/api/api";

@IonicPage()
@Component({
  selector: 'page-mass',
  templateUrl: 'mass.html'
})
export class MassPage implements OnInit {
  account: Account;
  public rep;
  avion;

  constructor(public navCtrl: NavController,
              private principal: Principal,
              private app: App,
              private loginService: LoginService,
              private api: Api,
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
      this.api.get("avions/3").subscribe(response => {
          this.avion = response;
          levierBagages = this.avion.levierBagages;
          levierAvant = this.avion.levierPassagersAvant;
          levierArriere = this.avion.levierPassagersArriere;
          levierReservoir = this.avion.reservoir.levier;
      });

  }

}
