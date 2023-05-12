import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormValidatorDetailComponent } from './form-validator-detail.component';

describe('FormValidator Management Detail Component', () => {
  let comp: FormValidatorDetailComponent;
  let fixture: ComponentFixture<FormValidatorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormValidatorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ formValidator: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FormValidatorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FormValidatorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load formValidator on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.formValidator).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
