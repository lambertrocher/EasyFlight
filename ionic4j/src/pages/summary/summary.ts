import { Component, OnInit } from '@angular/core';
import { App, IonicPage, NavController } from 'ionic-angular';
import { Principal } from '../../providers/auth/principal.service';
import { FirstRunPage } from '../pages';
import { LoginService } from '../../providers/login/login.service';

/**
 * Generated class for the SummaryPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-summary',
  templateUrl: 'summary.html',
})
export class SummaryPage implements OnInit {
    account: Account;

    constructor(public navCtrl: NavController,
                private principal: Principal,
                private app: App,
                private loginService: LoginService) { }

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

    summary() {
        this.navCtrl.push('SummaryPage');
    }

    preparation() {
	   this.navCtrl.push('PreparationPage');
    }
}
