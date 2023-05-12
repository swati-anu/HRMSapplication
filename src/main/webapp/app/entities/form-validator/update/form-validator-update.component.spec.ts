import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FormValidatorFormService } from './form-validator-form.service';
import { FormValidatorService } from '../service/form-validator.service';
import { IFormValidator } from '../form-validator.model';

import { FormValidatorUpdateComponent } from './form-validator-update.component';

describe('FormValidator Management Update Component', () => {
  let comp: FormValidatorUpdateComponent;
  let fixture: ComponentFixture<FormValidatorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formValidatorFormService: FormValidatorFormService;
  let formValidatorService: FormValidatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FormValidatorUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FormValidatorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormValidatorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formValidatorFormService = TestBed.inject(FormValidatorFormService);
    formValidatorService = TestBed.inject(FormValidatorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const formValidator: IFormValidator = { id: 456 };

      activatedRoute.data = of({ formValidator });
      comp.ngOnInit();

      expect(comp.formValidator).toEqual(formValidator);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormValidator>>();
      const formValidator = { id: 123 };
      jest.spyOn(formValidatorFormService, 'getFormValidator').mockReturnValue(formValidator);
      jest.spyOn(formValidatorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formValidator });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formValidator }));
      saveSubject.complete();

      // THEN
      expect(formValidatorFormService.getFormValidator).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formValidatorService.update).toHaveBeenCalledWith(expect.objectContaining(formValidator));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormValidator>>();
      const formValidator = { id: 123 };
      jest.spyOn(formValidatorFormService, 'getFormValidator').mockReturnValue({ id: null });
      jest.spyOn(formValidatorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formValidator: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formValidator }));
      saveSubject.complete();

      // THEN
      expect(formValidatorFormService.getFormValidator).toHaveBeenCalled();
      expect(formValidatorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormValidator>>();
      const formValidator = { id: 123 };
      jest.spyOn(formValidatorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formValidator });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formValidatorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
