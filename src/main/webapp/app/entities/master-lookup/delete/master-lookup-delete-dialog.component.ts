import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMasterLookup } from '../master-lookup.model';
import { MasterLookupService } from '../service/master-lookup.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './master-lookup-delete-dialog.component.html',
})
export class MasterLookupDeleteDialogComponent {
  masterLookup?: IMasterLookup;

  constructor(protected masterLookupService: MasterLookupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.masterLookupService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
