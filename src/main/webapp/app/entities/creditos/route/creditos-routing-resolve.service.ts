import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICreditos, Creditos } from '../creditos.model';
import { CreditosService } from '../service/creditos.service';

@Injectable({ providedIn: 'root' })
export class CreditosRoutingResolveService implements Resolve<ICreditos> {
  constructor(protected service: CreditosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICreditos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((creditos: HttpResponse<Creditos>) => {
          if (creditos.body) {
            return of(creditos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Creditos());
  }
}
