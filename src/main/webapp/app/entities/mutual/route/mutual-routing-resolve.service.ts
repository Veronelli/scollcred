import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMutual, Mutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';

@Injectable({ providedIn: 'root' })
export class MutualRoutingResolveService implements Resolve<IMutual> {
  constructor(protected service: MutualService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMutual> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mutual: HttpResponse<Mutual>) => {
          if (mutual.body) {
            return of(mutual.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mutual());
  }
}
