import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMasterLookup, NewMasterLookup } from '../master-lookup.model';

export type PartialUpdateMasterLookup = Partial<IMasterLookup> & Pick<IMasterLookup, 'id'>;

type RestOf<T extends IMasterLookup | NewMasterLookup> = Omit<T, 'lastModified'> & {
  lastModified?: string | null;
};

export type RestMasterLookup = RestOf<IMasterLookup>;

export type NewRestMasterLookup = RestOf<NewMasterLookup>;

export type PartialUpdateRestMasterLookup = RestOf<PartialUpdateMasterLookup>;

export type EntityResponseType = HttpResponse<IMasterLookup>;
export type EntityArrayResponseType = HttpResponse<IMasterLookup[]>;

@Injectable({ providedIn: 'root' })
export class MasterLookupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/master-lookups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(masterLookup: NewMasterLookup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(masterLookup);
    return this.http
      .post<RestMasterLookup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(masterLookup: IMasterLookup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(masterLookup);
    return this.http
      .put<RestMasterLookup>(`${this.resourceUrl}/${this.getMasterLookupIdentifier(masterLookup)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(masterLookup: PartialUpdateMasterLookup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(masterLookup);
    return this.http
      .patch<RestMasterLookup>(`${this.resourceUrl}/${this.getMasterLookupIdentifier(masterLookup)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMasterLookup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMasterLookup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMasterLookupIdentifier(masterLookup: Pick<IMasterLookup, 'id'>): number {
    return masterLookup.id;
  }

  compareMasterLookup(o1: Pick<IMasterLookup, 'id'> | null, o2: Pick<IMasterLookup, 'id'> | null): boolean {
    return o1 && o2 ? this.getMasterLookupIdentifier(o1) === this.getMasterLookupIdentifier(o2) : o1 === o2;
  }

  addMasterLookupToCollectionIfMissing<Type extends Pick<IMasterLookup, 'id'>>(
    masterLookupCollection: Type[],
    ...masterLookupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const masterLookups: Type[] = masterLookupsToCheck.filter(isPresent);
    if (masterLookups.length > 0) {
      const masterLookupCollectionIdentifiers = masterLookupCollection.map(
        masterLookupItem => this.getMasterLookupIdentifier(masterLookupItem)!
      );
      const masterLookupsToAdd = masterLookups.filter(masterLookupItem => {
        const masterLookupIdentifier = this.getMasterLookupIdentifier(masterLookupItem);
        if (masterLookupCollectionIdentifiers.includes(masterLookupIdentifier)) {
          return false;
        }
        masterLookupCollectionIdentifiers.push(masterLookupIdentifier);
        return true;
      });
      return [...masterLookupsToAdd, ...masterLookupCollection];
    }
    return masterLookupCollection;
  }

  protected convertDateFromClient<T extends IMasterLookup | NewMasterLookup | PartialUpdateMasterLookup>(masterLookup: T): RestOf<T> {
    return {
      ...masterLookup,
      lastModified: masterLookup.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMasterLookup: RestMasterLookup): IMasterLookup {
    return {
      ...restMasterLookup,
      lastModified: restMasterLookup.lastModified ? dayjs(restMasterLookup.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMasterLookup>): HttpResponse<IMasterLookup> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMasterLookup[]>): HttpResponse<IMasterLookup[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
