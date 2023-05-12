import dayjs from 'dayjs/esm';

export interface IFormValidator {
  id: number;
  type?: string | null;
  value?: string | null;
  formName?: string | null;
  fieldName?: string | null;
  companyId?: number | null;
  status?: string | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
}

export type NewFormValidator = Omit<IFormValidator, 'id'> & { id: null };
