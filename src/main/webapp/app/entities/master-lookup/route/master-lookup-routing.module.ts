import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MasterLookupComponent } from '../list/master-lookup.component';
import { MasterLookupDetailComponent } from '../detail/master-lookup-detail.component';
import { MasterLookupUpdateComponent } from '../update/master-lookup-update.component';
import { MasterLookupRoutingResolveService } from './master-lookup-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const masterLookupRoute: Routes = [
  {
    path: '',
    component: MasterLookupComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MasterLookupDetailComponent,
    resolve: {
      masterLookup: MasterLookupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MasterLookupUpdateComponent,
    resolve: {
      masterLookup: MasterLookupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MasterLookupUpdateComponent,
    resolve: {
      masterLookup: MasterLookupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(masterLookupRoute)],
  exports: [RouterModule],
})
export class MasterLookupRoutingModule {}
