import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MutualComponent } from './list/mutual.component';
import { MutualDetailComponent } from './detail/mutual-detail.component';
import { MutualUpdateComponent } from './update/mutual-update.component';
import { MutualDeleteDialogComponent } from './delete/mutual-delete-dialog.component';
import { MutualRoutingModule } from './route/mutual-routing.module';

@NgModule({
  imports: [SharedModule, MutualRoutingModule],
  declarations: [MutualComponent, MutualDetailComponent, MutualUpdateComponent, MutualDeleteDialogComponent],
  entryComponents: [MutualDeleteDialogComponent],
})
export class MutualModule {}
