import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDependencia, getDependenciaIdentifier } from '../dependencia.model';

export type EntityResponseType = HttpResponse<IDependencia>;
export type EntityArrayResponseType = HttpResponse<IDependencia[]>;

@Injectable({ providedIn: 'root' })
export class DependenciaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dependencias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dependencia: IDependencia): Observable<EntityResponseType> {
    return this.http.post<IDependencia>(this.resourceUrl, dependencia, { observe: 'response' });
  }

  update(dependencia: IDependencia): Observable<EntityResponseType> {
    return this.http.put<IDependencia>(`${this.resourceUrl}/${getDependenciaIdentifier(dependencia) as number}`, dependencia, {
      observe: 'response',
    });
  }

  partialUpdate(dependencia: IDependencia): Observable<EntityResponseType> {
    return this.http.patch<IDependencia>(`${this.resourceUrl}/${getDependenciaIdentifier(dependencia) as number}`, dependencia, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDependencia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDependencia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDependenciaToCollectionIfMissing(
    dependenciaCollection: IDependencia[],
    ...dependenciasToCheck: (IDependencia | null | undefined)[]
  ): IDependencia[] {
    const dependencias: IDependencia[] = dependenciasToCheck.filter(isPresent);
    if (dependencias.length > 0) {
      const dependenciaCollectionIdentifiers = dependenciaCollection.map(dependenciaItem => getDependenciaIdentifier(dependenciaItem)!);
      const dependenciasToAdd = dependencias.filter(dependenciaItem => {
        const dependenciaIdentifier = getDependenciaIdentifier(dependenciaItem);
        if (dependenciaIdentifier == null || dependenciaCollectionIdentifiers.includes(dependenciaIdentifier)) {
          return false;
        }
        dependenciaCollectionIdentifiers.push(dependenciaIdentifier);
        return true;
      });
      return [...dependenciasToAdd, ...dependenciaCollection];
    }
    return dependenciaCollection;
  }
}
