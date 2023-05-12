import dayjs from 'dayjs/esm';
import { IDesignation } from 'app/entities/designation/designation.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IBranch } from 'app/entities/branch/branch.model';
import { IRegion } from 'app/entities/region/region.model';

export interface IEmployee {
  id: number;
  firstName?: string | null;
  middleName?: string | null;
  lastName?: string | null;
  gender?: string | null;
  empUniqueId?: string | null;
  joindate?: dayjs.Dayjs | null;
  status?: string | null;
  emailId?: string | null;
  employmentTypeId?: number | null;
  reportingEmpId?: number | null;
  companyId?: number | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  designation?: Pick<IDesignation, 'id' | 'name'> | null;
  department?: Pick<IDepartment, 'id' | 'name'> | null;
  branch?: Pick<IBranch, 'id' | 'branchName'> | null;
  region?: Pick<IRegion, 'id' | 'regionName'> | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
