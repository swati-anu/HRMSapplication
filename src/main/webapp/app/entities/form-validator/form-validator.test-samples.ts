import dayjs from 'dayjs/esm';

import { IFormValidator, NewFormValidator } from './form-validator.model';

export const sampleWithRequiredData: IFormValidator = {
  id: 37814,
};

export const sampleWithPartialData: IFormValidator = {
  id: 95162,
  formName: 'Incredible 24/7 Buckinghamshire',
  fieldName: 'teal',
  lastModified: dayjs('2023-05-12T13:17'),
  lastModifiedBy: 'blue Cheese empower',
};

export const sampleWithFullData: IFormValidator = {
  id: 76177,
  type: 'Paradigm haptic',
  value: 'Fields',
  formName: 'Electronics bypassing drive',
  fieldName: 'e-enable quantifying',
  companyId: 7829,
  status: 'reboot Handcrafted Practical',
  lastModified: dayjs('2023-05-11T17:08'),
  lastModifiedBy: 'Architect',
};

export const sampleWithNewData: NewFormValidator = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
