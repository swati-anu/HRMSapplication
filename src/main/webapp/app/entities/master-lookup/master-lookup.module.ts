import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MasterLookupComponent } from './list/master-lookup.component';
import { MasterLookupDetailComponent } from './detail/master-lookup-detail.component';
import { MasterLookupUpdateComponent } from './update/master-lookup-update.component';
import { MasterLookupDeleteDialogComponent } from './delete/master-lookup-delete-dialog.component';
import { MasterLookupRoutingModule } from './route/master-lookup-routing.module';

@NgModule({
  imports: [SharedModule, MasterLookupRoutingModule],
  declarations: [MasterLookupComponent, MasterLookupDetailComponent, MasterLookupUpdateComponent, MasterLookupDeleteDialogComponent],
})
export class MasterLookupModule {}
