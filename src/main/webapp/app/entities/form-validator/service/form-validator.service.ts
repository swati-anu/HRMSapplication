import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormValidator, NewFormValidator } from '../form-validator.model';

export type PartialUpdateFormValidator = Partial<IFormValidator> & Pick<IFormValidator, 'id'>;

type RestOf<T extends IFormValidator | NewFormValidator> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

export type RestFormValidator = RestOf<IFormValidator>;

export type NewRestFormValidator = RestOf<NewFormValidator>;

export type PartialUpdateRestFormValidator = RestOf<PartialUpdateFormValidator>;

export type EntityResponseType = HttpResponse<IFormValidator>;
export type EntityArrayResponseType = HttpResponse<IFormValidator[]>;

@Injectable({ providedIn: 'root' })
export class FormValidatorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/form-validators');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(formValidator: NewFormValidator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formValidator);
    return this.http
      .post<RestFormValidator>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(formValidator: IFormValidator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formValidator);
    return this.http
      .put<RestFormValidator>(`${this.resourceUrl}/${this.getFormValidatorIdentifier(formValidator)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(formValidator: PartialUpdateFormValidator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formValidator);
    return this.http
      .patch<RestFormValidator>(`${this.resourceUrl}/${this.getFormValidatorIdentifier(formValidator)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFormValidator>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFormValidator[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFormValidatorIdentifier(formValidator: Pick<IFormValidator, 'id'>): number {
    return formValidator.id;
  }

  compareFormValidator(o1: Pick<IFormValidator, 'id'> | null, o2: Pick<IFormValidator, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormValidatorIdentifier(o1) === this.getFormValidatorIdentifier(o2) : o1 === o2;
  }

  addFormValidatorToCollectionIfMissing<Type extends Pick<IFormValidator, 'id'>>(
    formValidatorCollection: Type[],
    ...formValidatorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formValidators: Type[] = formValidatorsToCheck.filter(isPresent);
    if (formValidators.length > 0) {
      const formValidatorCollectionIdentifiers = formValidatorCollection.map(
        formValidatorItem => this.getFormValidatorIdentifier(formValidatorItem)!
      );
      const formValidatorsToAdd = formValidators.filter(formValidatorItem => {
        const formValidatorIdentifier = this.getFormValidatorIdentifier(formValidatorItem);
        if (formValidatorCollectionIdentifiers.includes(formValidatorIdentifier)) {
          return false;
        }
        formValidatorCollectionIdentifiers.push(formValidatorIdentifier);
        return true;
      });
      return [...formValidatorsToAdd, ...formValidatorCollection];
    }
    return formValidatorCollection;
  }

  protected convertDateFromClient<T extends IFormValidator | NewFormValidator | PartialUpdateFormValidator>(formValidator: T): RestOf<T> {
    return {
      ...formValidator,
      lastModified: formValidator.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFormValidator: RestFormValidator): IFormValidator {
    return {
      ...restFormValidator,
      lastModified: restFormValidator.lastModified ? dayjs(restFormValidator.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFormValidator>): HttpResponse<IFormValidator> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFormValidator[]>): HttpResponse<IFormValidator[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
