import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormValidator } from '../form-validator.model';
import { FormValidatorService } from '../service/form-validator.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './form-validator-delete-dialog.component.html',
})
export class FormValidatorDeleteDialogComponent {
  formValidator?: IFormValidator;

  constructor(protected formValidatorService: FormValidatorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formValidatorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
