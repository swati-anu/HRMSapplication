import dayjs from 'dayjs/esm';

import { IMasterLookup, NewMasterLookup } from './master-lookup.model';

export const sampleWithRequiredData: IMasterLookup = {
  id: 63271,
};

export const sampleWithPartialData: IMasterLookup = {
  id: 30535,
  value: 'Lead niches',
  valueTwo: 'collaboration edge port',
  description: 'communities Intelligent homogeneous',
  status: 'Research',
  lastModifiedBy: 'purple',
};

export const sampleWithFullData: IMasterLookup = {
  id: 65505,
  name: 'transmitting',
  value: 'index index',
  valueTwo: 'bifurcated Facilitator',
  description: 'Orchestrator Automotive',
  type: 'revolutionize',
  companyId: 91062,
  status: 'Borders Future-proofed Savings',
  lastModified: dayjs('2023-05-11T15:41'),
  lastModifiedBy: 'Manager',
};

export const sampleWithNewData: NewMasterLookup = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
