import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MutualComponent } from '../list/mutual.component';
import { MutualDetailComponent } from '../detail/mutual-detail.component';
import { MutualUpdateComponent } from '../update/mutual-update.component';
import { MutualRoutingResolveService } from './mutual-routing-resolve.service';

const mutualRoute: Routes = [
  {
    path: '',
    component: MutualComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MutualDetailComponent,
    resolve: {
      mutual: MutualRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MutualUpdateComponent,
    resolve: {
      mutual: MutualRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MutualUpdateComponent,
    resolve: {
      mutual: MutualRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mutualRoute)],
  exports: [RouterModule],
})
export class MutualRoutingModule {}
