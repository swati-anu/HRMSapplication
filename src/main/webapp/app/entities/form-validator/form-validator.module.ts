import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormValidatorComponent } from './list/form-validator.component';
import { FormValidatorDetailComponent } from './detail/form-validator-detail.component';
import { FormValidatorUpdateComponent } from './update/form-validator-update.component';
import { FormValidatorDeleteDialogComponent } from './delete/form-validator-delete-dialog.component';
import { FormValidatorRoutingModule } from './route/form-validator-routing.module';

@NgModule({
  imports: [SharedModule, FormValidatorRoutingModule],
  declarations: [FormValidatorComponent, FormValidatorDetailComponent, FormValidatorUpdateComponent, FormValidatorDeleteDialogComponent],
})
export class FormValidatorModule {}
