import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormValidator } from '../form-validator.model';

@Component({
  selector: 'jhi-form-validator-detail',
  templateUrl: './form-validator-detail.component.html',
})
export class FormValidatorDetailComponent implements OnInit {
  formValidator: IFormValidator | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formValidator }) => {
      this.formValidator = formValidator;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
