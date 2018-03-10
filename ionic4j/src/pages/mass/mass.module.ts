import { NgModule } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { IonicPageModule } from 'ionic-angular';

import { MassPage } from './mass';

@NgModule({
  declarations: [
    MassPage
  ],
  imports: [
    IonicPageModule.forChild(MassPage),
    TranslateModule.forChild()
  ],
  exports: [
    MassPage
  ]
})
export class MassPageModule { }
