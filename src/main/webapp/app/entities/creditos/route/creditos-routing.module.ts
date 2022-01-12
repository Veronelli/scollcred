import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CreditosComponent } from '../list/creditos.component';
import { CreditosDetailComponent } from '../detail/creditos-detail.component';
import { CreditosUpdateComponent } from '../update/creditos-update.component';
import { CreditosRoutingResolveService } from './creditos-routing-resolve.service';

const creditosRoute: Routes = [
  {
    path: '',
    component: CreditosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CreditosDetailComponent,
    resolve: {
      creditos: CreditosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CreditosUpdateComponent,
    resolve: {
      creditos: CreditosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CreditosUpdateComponent,
    resolve: {
      creditos: CreditosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(creditosRoute)],
  exports: [RouterModule],
})
export class CreditosRoutingModule {}
