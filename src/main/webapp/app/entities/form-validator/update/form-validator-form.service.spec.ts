import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../form-validator.test-samples';

import { FormValidatorFormService } from './form-validator-form.service';

describe('FormValidator Form Service', () => {
  let service: FormValidatorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormValidatorFormService);
  });

  describe('Service methods', () => {
    describe('createFormValidatorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFormValidatorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            value: expect.any(Object),
            formName: expect.any(Object),
            fieldName: expect.any(Object),
            companyId: expect.any(Object),
            status: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });

      it('passing IFormValidator should create a new form with FormGroup', () => {
        const formGroup = service.createFormValidatorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            value: expect.any(Object),
            formName: expect.any(Object),
            fieldName: expect.any(Object),
            companyId: expect.any(Object),
            status: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getFormValidator', () => {
      it('should return NewFormValidator for default FormValidator initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFormValidatorFormGroup(sampleWithNewData);

        const formValidator = service.getFormValidator(formGroup) as any;

        expect(formValidator).toMatchObject(sampleWithNewData);
      });

      it('should return NewFormValidator for empty FormValidator initial value', () => {
        const formGroup = service.createFormValidatorFormGroup();

        const formValidator = service.getFormValidator(formGroup) as any;

        expect(formValidator).toMatchObject({});
      });

      it('should return IFormValidator', () => {
        const formGroup = service.createFormValidatorFormGroup(sampleWithRequiredData);

        const formValidator = service.getFormValidator(formGroup) as any;

        expect(formValidator).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFormValidator should not enable id FormControl', () => {
        const formGroup = service.createFormValidatorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFormValidator should disable id FormControl', () => {
        const formGroup = service.createFormValidatorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
