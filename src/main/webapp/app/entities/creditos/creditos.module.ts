import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CreditosComponent } from './list/creditos.component';
import { CreditosDetailComponent } from './detail/creditos-detail.component';
import { CreditosUpdateComponent } from './update/creditos-update.component';
import { CreditosDeleteDialogComponent } from './delete/creditos-delete-dialog.component';
import { CreditosRoutingModule } from './route/creditos-routing.module';

@NgModule({
  imports: [SharedModule, CreditosRoutingModule],
  declarations: [CreditosComponent, CreditosDetailComponent, CreditosUpdateComponent, CreditosDeleteDialogComponent],
  entryComponents: [CreditosDeleteDialogComponent],
})
export class CreditosModule {}
