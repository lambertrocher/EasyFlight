import { Component, OnInit } from '@angular/core';
import { App, IonicPage, NavController } from 'ionic-angular';
import { Principal } from '../../providers/auth/principal.service';
import { FirstRunPage } from '../pages';
import { LoginService } from '../../providers/login/login.service';
import { Api} from "../../providers/api/api";
import {PreparationProvider} from "../../providers/preparation/preparation";

/**
 * Generated class for the MeteoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-meteo',
  templateUrl: 'meteo.html',
})
export class MeteoPage implements OnInit {
    account: Account;
    list_meteo;

    constructor(public navCtrl: NavController,
                private principal: Principal,
                private app: App,
                private loginService: LoginService,
                private api: Api,
                private preparationProvider: PreparationProvider,) { }

    ngOnInit() {
        this.principal.identity().then((account) => {
            if (account === null) {
                this.app.getRootNavs()[0].setRoot(FirstRunPage);
            } else {
                this.account = account;
            }
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    logout() {
        this.loginService.logout();
        this.app.getRootNavs()[0].setRoot(FirstRunPage);
    }

    meteo() {
        this.navCtrl.push('MeteoPage');
    }

    update_meteo(){

        let airport_list = [];
        airport_list = this.preparationProvider.airport;
        console.log("liste des aérports",airport_list);
        if (airport_list.length > 0) {
        let param_airport = "";
            for (let airport of airport_list) {
                console.log(airport);
                param_airport = param_airport + ',' + airport;
            }
            param_airport = param_airport.substr(1);
            console.log("metar/" + param_airport);
            this.api.get("metar/" + param_airport).subscribe(response => {
                    this.list_meteo = response;
                console.log(this.list_meteo);
            });
        }
        else{
            console.log("pas d'aéroport");
            this.list_meteo = "";
        }
    }

    ionViewDidEnter(){
        this.update_meteo();
        console.log("enter meteo");
    }
}

