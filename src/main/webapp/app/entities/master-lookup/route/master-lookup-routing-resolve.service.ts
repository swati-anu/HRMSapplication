import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMasterLookup } from '../master-lookup.model';
import { MasterLookupService } from '../service/master-lookup.service';

@Injectable({ providedIn: 'root' })
export class MasterLookupRoutingResolveService implements Resolve<IMasterLookup | null> {
  constructor(protected service: MasterLookupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMasterLookup | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((masterLookup: HttpResponse<IMasterLookup>) => {
          if (masterLookup.body) {
            return of(masterLookup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
