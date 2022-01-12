import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDependencia, Dependencia } from '../dependencia.model';
import { DependenciaService } from '../service/dependencia.service';

@Injectable({ providedIn: 'root' })
export class DependenciaRoutingResolveService implements Resolve<IDependencia> {
  constructor(protected service: DependenciaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDependencia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dependencia: HttpResponse<Dependencia>) => {
          if (dependencia.body) {
            return of(dependencia.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dependencia());
  }
}
