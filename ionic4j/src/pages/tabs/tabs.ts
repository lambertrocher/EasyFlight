import { Component } from '@angular/core';
import { IonicPage } from 'ionic-angular';


@IonicPage()
@Component({
  selector: 'page-tabs',
  templateUrl: 'tabs.html'
})
export class TabsPage {
  // tab1Root: any = Tab1Root;
  // tab2Root: any = Tab2Root;
  // tab3Root: any = Tab3Root;
  // tab4Root: any = Tab4Root;
  //
  // tab1Title = " ";
  // tab2Title = " ";
  // tab3Title = " ";
  // tab4Title = " ";
  //
  // constructor(public navCtrl: NavController, public translateService: TranslateService) {
  //   translateService.get(['TAB1_TITLE', 'TAB2_TITLE', 'TAB3_TITLE', 'TAB4_TITLE']).subscribe(values => {
  //     this.tab1Title = values['TAB1_TITLE'];
  //     this.tab2Title = values['TAB2_TITLE'];
  //     this.tab3Title = values['TAB3_TITLE'];
  //     this.tab4Title = values['TAB4_TITLE'];
  //   });
  // }

    tab1Root = 'PreparationPage';
    tab2Root = 'MeteoPage';
    tab3Root = 'NotamsPage';
    tab4Root = 'MassPage';

    constructor() {

    }

}
