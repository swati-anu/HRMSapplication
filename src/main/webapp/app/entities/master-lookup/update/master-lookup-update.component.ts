import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MasterLookupFormService, MasterLookupFormGroup } from './master-lookup-form.service';
import { IMasterLookup } from '../master-lookup.model';
import { MasterLookupService } from '../service/master-lookup.service';

@Component({
  selector: 'jhi-master-lookup-update',
  templateUrl: './master-lookup-update.component.html',
})
export class MasterLookupUpdateComponent implements OnInit {
  isSaving = false;
  masterLookup: IMasterLookup | null = null;

  editForm: MasterLookupFormGroup = this.masterLookupFormService.createMasterLookupFormGroup();

  constructor(
    protected masterLookupService: MasterLookupService,
    protected masterLookupFormService: MasterLookupFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ masterLookup }) => {
      this.masterLookup = masterLookup;
      if (masterLookup) {
        this.updateForm(masterLookup);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const masterLookup = this.masterLookupFormService.getMasterLookup(this.editForm);
    if (masterLookup.id !== null) {
      this.subscribeToSaveResponse(this.masterLookupService.update(masterLookup));
    } else {
      this.subscribeToSaveResponse(this.masterLookupService.create(masterLookup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMasterLookup>>): void {
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

  protected updateForm(masterLookup: IMasterLookup): void {
    this.masterLookup = masterLookup;
    this.masterLookupFormService.resetForm(this.editForm, masterLookup);
  }
}
