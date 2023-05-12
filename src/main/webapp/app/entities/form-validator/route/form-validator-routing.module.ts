import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormValidatorComponent } from '../list/form-validator.component';
import { FormValidatorDetailComponent } from '../detail/form-validator-detail.component';
import { FormValidatorUpdateComponent } from '../update/form-validator-update.component';
import { FormValidatorRoutingResolveService } from './form-validator-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const formValidatorRoute: Routes = [
  {
    path: '',
    component: FormValidatorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormValidatorDetailComponent,
    resolve: {
      formValidator: FormValidatorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormValidatorUpdateComponent,
    resolve: {
      formValidator: FormValidatorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormValidatorUpdateComponent,
    resolve: {
      formValidator: FormValidatorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formValidatorRoute)],
  exports: [RouterModule],
})
export class FormValidatorRoutingModule {}
