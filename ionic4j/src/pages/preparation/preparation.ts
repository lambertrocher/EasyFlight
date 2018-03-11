import { Component, OnInit } from '@angular/core';
import { App, IonicPage, NavController } from 'ionic-angular';
import { Principal } from '../../providers/auth/principal.service';
import { FirstRunPage } from '../pages';
import { LoginService } from '../../providers/login/login.service';
import {WheelSelector} from "@ionic-native/wheel-selector";

/**
 * Generated class for the PreparationPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-preparation',
  templateUrl: 'preparation.html',
})
export class PreparationPage implements OnInit {
    account: Account;

    constructor(public navCtrl: NavController,
                private principal: Principal,
                private app: App,
                private loginService: LoginService,
                private selector: WheelSelector) { }

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

    preparation() {
        this.navCtrl.push('PreparationPage');
    }

    public event = {
        month: '2018-01-01',
        timeStarts: '07:43',
        timeEnds: '1990-02-20'
    };

    public poids_pilote: number = 0;
    public poids_passager1: number = 0;
    public poids_passager2: number = 0;
    public poids_passager3: number = 0;


    jsonData = {
    numbers: [
        { description: "1" },
        { description: "2" },
        { description: "3" }
    ]};

    selectANumber() {
        this.selector.show({
            title: "How Many?",
            items: [
                this.jsonData.numbers
            ],
        }).then(
            result => {
                console.log(result[0].description + ' at index: ' + result[0].index);
            },
            err => console.log('Error: ', err)
        );
    }











}