import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMasterLookup, NewMasterLookup } from '../master-lookup.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMasterLookup for edit and NewMasterLookupFormGroupInput for create.
 */
type MasterLookupFormGroupInput = IMasterLookup | PartialWithRequiredKeyOf<NewMasterLookup>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMasterLookup | NewMasterLookup> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

type MasterLookupFormRawValue = FormValueOf<IMasterLookup>;

type NewMasterLookupFormRawValue = FormValueOf<NewMasterLookup>;

type MasterLookupFormDefaults = Pick<NewMasterLookup, 'id' | 'lastModified'>;

type MasterLookupFormGroupContent = {
  id: FormControl<MasterLookupFormRawValue['id'] | NewMasterLookup['id']>;
  name: FormControl<MasterLookupFormRawValue['name']>;
  value: FormControl<MasterLookupFormRawValue['value']>;
  valueTwo: FormControl<MasterLookupFormRawValue['valueTwo']>;
  description: FormControl<MasterLookupFormRawValue['description']>;
  type: FormControl<MasterLookupFormRawValue['type']>;
  companyId: FormControl<MasterLookupFormRawValue['companyId']>;
  status: FormControl<MasterLookupFormRawValue['status']>;
  lastModified: FormControl<MasterLookupFormRawValue['lastModified']>;
  lastModifiedBy: FormControl<MasterLookupFormRawValue['lastModifiedBy']>;
};

export type MasterLookupFormGroup = FormGroup<MasterLookupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MasterLookupFormService {
  createMasterLookupFormGroup(masterLookup: MasterLookupFormGroupInput = { id: null }): MasterLookupFormGroup {
    const masterLookupRawValue = this.convertMasterLookupToMasterLookupRawValue({
      ...this.getFormDefaults(),
      ...masterLookup,
    });
    return new FormGroup<MasterLookupFormGroupContent>({
      id: new FormControl(
        { value: masterLookupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(masterLookupRawValue.name),
      value: new FormControl(masterLookupRawValue.value),
      valueTwo: new FormControl(masterLookupRawValue.valueTwo),
      description: new FormControl(masterLookupRawValue.description),
      type: new FormControl(masterLookupRawValue.type),
      companyId: new FormControl(masterLookupRawValue.companyId),
      status: new FormControl(masterLookupRawValue.status),
      lastModified: new FormControl(masterLookupRawValue.lastModified),
      lastModifiedBy: new FormControl(masterLookupRawValue.lastModifiedBy),
    });
  }

  getMasterLookup(form: MasterLookupFormGroup): IMasterLookup | NewMasterLookup {
    return this.convertMasterLookupRawValueToMasterLookup(form.getRawValue() as MasterLookupFormRawValue | NewMasterLookupFormRawValue);
  }

  resetForm(form: MasterLookupFormGroup, masterLookup: MasterLookupFormGroupInput): void {
    const masterLookupRawValue = this.convertMasterLookupToMasterLookupRawValue({ ...this.getFormDefaults(), ...masterLookup });
    form.reset(
      {
        ...masterLookupRawValue,
        id: { value: masterLookupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MasterLookupFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastModified: currentTime,
    };
  }

  private convertMasterLookupRawValueToMasterLookup(
    rawMasterLookup: MasterLookupFormRawValue | NewMasterLookupFormRawValue
  ): IMasterLookup | NewMasterLookup {
    return {
      ...rawMasterLookup,
      lastModified: dayjs(rawMasterLookup.lastModified, DATE_TIME_FORMAT),
    };
  }

  private convertMasterLookupToMasterLookupRawValue(
    masterLookup: IMasterLookup | (Partial<NewMasterLookup> & MasterLookupFormDefaults)
  ): MasterLookupFormRawValue | PartialWithRequiredKeyOf<NewMasterLookupFormRawValue> {
    return {
      ...masterLookup,
      lastModified: masterLookup.lastModified ? masterLookup.lastModified.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
