import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DependenciaComponent } from '../list/dependencia.component';
import { DependenciaDetailComponent } from '../detail/dependencia-detail.component';
import { DependenciaUpdateComponent } from '../update/dependencia-update.component';
import { DependenciaRoutingResolveService } from './dependencia-routing-resolve.service';

const dependenciaRoute: Routes = [
  {
    path: '',
    component: DependenciaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DependenciaDetailComponent,
    resolve: {
      dependencia: DependenciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DependenciaUpdateComponent,
    resolve: {
      dependencia: DependenciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DependenciaUpdateComponent,
    resolve: {
      dependencia: DependenciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dependenciaRoute)],
  exports: [RouterModule],
})
export class DependenciaRoutingModule {}
