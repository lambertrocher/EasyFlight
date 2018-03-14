import { Component, OnInit } from '@angular/core';
import { App, IonicPage, NavController } from 'ionic-angular';
import { Principal } from '../../providers/auth/principal.service';
import { FirstRunPage } from '../pages';
import { LoginService } from '../../providers/login/login.service';
import { Api} from "../../providers/api/api";
import {PreparationProvider} from "../../providers/preparation/preparation";

/**
 * Generated class for the NotamsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-notams',
  templateUrl: 'notams.html',
})
export class NotamsPage implements OnInit {
    account: Account;
    list_notams;
    nb_notams;
    

    constructor(public navCtrl: NavController,
                private principal: Principal,
                private app: App,
                private loginService: LoginService,
                private api: Api,
                private preparationProvider: PreparationProvider,
    ) { }

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

    notams() {
        this.navCtrl.push('NotamsPage');
    }

    update_notams(){

        let airport_list;
        airport_list = this.preparationProvider.airport;
        console.log("liste des aérports",airport_list);
        if (airport_list.length > 0) {
        let param_airport = "";
        for (let airport of airport_list) {
            console.log(airport);
            param_airport = param_airport + ',' + airport;
        }
        param_airport = param_airport.substr(1);
        console.log("notam/" + param_airport);
            this.api.get("notam/" + param_airport).subscribe(response => {
                this.list_notams = response;
                console.log(this.list_notams);
                this.nb_notams = this.list_notams.rows;
                console.log(this.nb_notams);
            });
        }
        else{
            console.log("pas d'aéroport");
            this.list_notams = "";
        }
    }

    ionViewDidEnter(){
        this.update_notams();
        console.log("enter notams");
    }
}