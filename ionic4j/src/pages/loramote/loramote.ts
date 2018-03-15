import { Component, OnInit } from '@angular/core';
import { App, IonicPage, NavController } from 'ionic-angular';
import { Principal } from '../../providers/auth/principal.service';
import { FirstRunPage } from '../pages';
import { LoginService } from '../../providers/login/login.service';
import { Api} from "../../providers/api/api";


/**
 * Generated class for the LoramotePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-loramote',
  templateUrl: 'loramote.html',
})
export class LoramotePage implements OnInit {
    account: Account;
    data;

    constructor(public navCtrl: NavController,
                private principal: Principal,
                private app: App,
                private loginService: LoginService,
                private api: Api,
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

    loramote() {
        this.navCtrl.push('LoramotePage');
    }

     update_loramote(){
        console.log("loramote/" + "1");
        this.api.get("loramote/" + "1").subscribe(response => {
                this.data = response;
            console.log(this.data);
        });
    }

    ionViewDidEnter(){
        console.log("Enter LORAMOTE");
        this.update_loramote();
        console.log("enter loramote");
    }
}

