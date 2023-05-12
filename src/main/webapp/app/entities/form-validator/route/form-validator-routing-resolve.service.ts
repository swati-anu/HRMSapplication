import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormValidator } from '../form-validator.model';
import { FormValidatorService } from '../service/form-validator.service';

@Injectable({ providedIn: 'root' })
export class FormValidatorRoutingResolveService implements Resolve<IFormValidator | null> {
  constructor(protected service: FormValidatorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormValidator | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formValidator: HttpResponse<IFormValidator>) => {
          if (formValidator.body) {
            return of(formValidator.body);
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
