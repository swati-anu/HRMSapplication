import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FormValidatorFormService, FormValidatorFormGroup } from './form-validator-form.service';
import { IFormValidator } from '../form-validator.model';
import { FormValidatorService } from '../service/form-validator.service';

@Component({
  selector: 'jhi-form-validator-update',
  templateUrl: './form-validator-update.component.html',
})
export class FormValidatorUpdateComponent implements OnInit {
  isSaving = false;
  formValidator: IFormValidator | null = null;

  editForm: FormValidatorFormGroup = this.formValidatorFormService.createFormValidatorFormGroup();

  constructor(
    protected formValidatorService: FormValidatorService,
    protected formValidatorFormService: FormValidatorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formValidator }) => {
      this.formValidator = formValidator;
      if (formValidator) {
        this.updateForm(formValidator);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formValidator = this.formValidatorFormService.getFormValidator(this.editForm);
    if (formValidator.id !== null) {
      this.subscribeToSaveResponse(this.formValidatorService.update(formValidator));
    } else {
      this.subscribeToSaveResponse(this.formValidatorService.create(formValidator));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormValidator>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(formValidator: IFormValidator): void {
    this.formValidator = formValidator;
    this.formValidatorFormService.resetForm(this.editForm, formValidator);
  }
}
