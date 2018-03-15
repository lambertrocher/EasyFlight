import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { LoramotePage } from './loramote';

@NgModule({
  declarations: [
    LoramotePage,
  ],
  imports: [
    IonicPageModule.forChild(LoramotePage),
  ],
})
export class LoramotePageModule {}
