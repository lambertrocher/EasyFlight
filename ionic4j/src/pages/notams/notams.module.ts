import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { NotamsPage } from './notams';

@NgModule({
  declarations: [
    NotamsPage,
  ],
  imports: [
      IonicPageModule.forChild(NotamsPage),
  ],
  exports: [
      NotamsPage
  ]
})
export class NotamsPageModule {}
