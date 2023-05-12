import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../master-lookup.test-samples';

import { MasterLookupFormService } from './master-lookup-form.service';

describe('MasterLookup Form Service', () => {
  let service: MasterLookupFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MasterLookupFormService);
  });

  describe('Service methods', () => {
    describe('createMasterLookupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMasterLookupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            valueTwo: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            companyId: expect.any(Object),
            status: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });

      it('passing IMasterLookup should create a new form with FormGroup', () => {
        const formGroup = service.createMasterLookupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            valueTwo: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            companyId: expect.any(Object),
            status: expect.any(Object),
            lastModified: expect.any(Object),
            lastModifiedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getMasterLookup', () => {
      it('should return NewMasterLookup for default MasterLookup initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMasterLookupFormGroup(sampleWithNewData);

        const masterLookup = service.getMasterLookup(formGroup) as any;

        expect(masterLookup).toMatchObject(sampleWithNewData);
      });

      it('should return NewMasterLookup for empty MasterLookup initial value', () => {
        const formGroup = service.createMasterLookupFormGroup();

        const masterLookup = service.getMasterLookup(formGroup) as any;

        expect(masterLookup).toMatchObject({});
      });

      it('should return IMasterLookup', () => {
        const formGroup = service.createMasterLookupFormGroup(sampleWithRequiredData);

        const masterLookup = service.getMasterLookup(formGroup) as any;

        expect(masterLookup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMasterLookup should not enable id FormControl', () => {
        const formGroup = service.createMasterLookupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMasterLookup should disable id FormControl', () => {
        const formGroup = service.createMasterLookupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
