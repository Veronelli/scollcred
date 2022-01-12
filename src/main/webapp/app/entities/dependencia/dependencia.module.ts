import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DependenciaComponent } from './list/dependencia.component';
import { DependenciaDetailComponent } from './detail/dependencia-detail.component';
import { DependenciaUpdateComponent } from './update/dependencia-update.component';
import { DependenciaDeleteDialogComponent } from './delete/dependencia-delete-dialog.component';
import { DependenciaRoutingModule } from './route/dependencia-routing.module';

@NgModule({
  imports: [SharedModule, DependenciaRoutingModule],
  declarations: [DependenciaComponent, DependenciaDetailComponent, DependenciaUpdateComponent, DependenciaDeleteDialogComponent],
  entryComponents: [DependenciaDeleteDialogComponent],
})
export class DependenciaModule {}
