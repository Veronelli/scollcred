import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMutual, getMutualIdentifier } from '../mutual.model';

export type EntityResponseType = HttpResponse<IMutual>;
export type EntityArrayResponseType = HttpResponse<IMutual[]>;

@Injectable({ providedIn: 'root' })
export class MutualService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mutuals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mutual: IMutual): Observable<EntityResponseType> {
    return this.http.post<IMutual>(this.resourceUrl, mutual, { observe: 'response' });
  }

  update(mutual: IMutual): Observable<EntityResponseType> {
    return this.http.put<IMutual>(`${this.resourceUrl}/${getMutualIdentifier(mutual) as number}`, mutual, { observe: 'response' });
  }

  partialUpdate(mutual: IMutual): Observable<EntityResponseType> {
    return this.http.patch<IMutual>(`${this.resourceUrl}/${getMutualIdentifier(mutual) as number}`, mutual, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMutual>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMutual[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMutualToCollectionIfMissing(mutualCollection: IMutual[], ...mutualsToCheck: (IMutual | null | undefined)[]): IMutual[] {
    const mutuals: IMutual[] = mutualsToCheck.filter(isPresent);
    if (mutuals.length > 0) {
      const mutualCollectionIdentifiers = mutualCollection.map(mutualItem => getMutualIdentifier(mutualItem)!);
      const mutualsToAdd = mutuals.filter(mutualItem => {
        const mutualIdentifier = getMutualIdentifier(mutualItem);
        if (mutualIdentifier == null || mutualCollectionIdentifiers.includes(mutualIdentifier)) {
          return false;
        }
        mutualCollectionIdentifiers.push(mutualIdentifier);
        return true;
      });
      return [...mutualsToAdd, ...mutualCollection];
    }
    return mutualCollection;
  }
}
