import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import { App, IonicPage, NavController } from 'ionic-angular';
import { Principal } from '../../providers/auth/principal.service';
import { FirstRunPage } from '../pages';
import { LoginService } from '../../providers/login/login.service';
import { Api } from "../../providers/api/api"
import {PreparationPage} from "../preparation/preparation";
import {PreparationProvider} from "../../providers/preparation/preparation";
import {Chart} from 'chart.js';

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
  poidsTotal;
  chart = [];


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
          this.poidsTotal = masseVideAvion + poidsPilote + poidsPassager1 + poidsPassager2 + poidsPassager3 + poidsBagages;
          console.log("moment total", momentTotal);
          this.brasLevierTotal = momentTotal / this.poidsTotal;
          console.log(this.brasLevierTotal);
          console.log("poids total", this.poidsTotal);
          this.graphs();
      });
  }



  graphs(){
      this.chart = new Chart('canvas', {
          type: 'line',
          data: {
              datasets: [{
                  label: "My second dataset",
                  fillColor: "#c28a18",
                  strokeColor: "#c28a18",
                  highlightFill: "#c28a18",
                  highlightStroke: "#c28a18",
                  backgroundColor: ['#c28a18'],
                  pointRadius: 8,
                  data: [{x:this.brasLevierTotal,y:this.poidsTotal}] // Note the structure change here!
              },
                  {
                      label: "My First dataset",
                      fillColor: "#c28a18",
                      strokeColor: "#c28a18",
                      highlightFill: "#c28a18",
                      highlightStroke: "#c28a18",
                      backgroundColor: ['#ededed'],
                      pointRadius: 0,
                      data: [{x:0.205,y:750},{x:0.428,y:1000},{x:0.450,y:1000},{x:0.500,y:1000},{x:0.564,y:1000}] // Note the structure change here!
                  },
              ]
          },
          options: {
              legend: {
                  display: false
              },
              scales: {
                  xAxes: [{
                      display: true,
                      type: 'linear',
                      ticks: {
                          min: 0.15,
                          max: 0.6,
                      }
                  }],
                  yAxes: [{
                      display: true,
                      label:"bras de levier",
                      ticks: {
                          min: 500,
                          max: 1200,
                      }
                  }],
              }
          }
      });
  }


    ionViewDidEnter(){
      this.masse_et_centrage();
  }


}
