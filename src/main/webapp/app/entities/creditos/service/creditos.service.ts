import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICreditos, getCreditosIdentifier } from '../creditos.model';

export type EntityResponseType = HttpResponse<ICreditos>;
export type EntityArrayResponseType = HttpResponse<ICreditos[]>;

@Injectable({ providedIn: 'root' })
export class CreditosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/creditos');
  private modalFilter = false;

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  get getModalFilter(): boolean {
    return this.modalFilter;
  }

  setModalFilter(value: boolean): void {
    this.modalFilter = !value;
  }

  create(creditos: ICreditos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(creditos);
    return this.http
      .post<ICreditos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(creditos: ICreditos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(creditos);
    return this.http
      .put<ICreditos>(`${this.resourceUrl}/${getCreditosIdentifier(creditos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(creditos: ICreditos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(creditos);
    return this.http
      .patch<ICreditos>(`${this.resourceUrl}/${getCreditosIdentifier(creditos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICreditos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICreditos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCreditosToCollectionIfMissing(creditosCollection: ICreditos[], ...creditosToCheck: (ICreditos | null | undefined)[]): ICreditos[] {
    const creditos: ICreditos[] = creditosToCheck.filter(isPresent);
    if (creditos.length > 0) {
      const creditosCollectionIdentifiers = creditosCollection.map(creditosItem => getCreditosIdentifier(creditosItem)!);
      const creditosToAdd = creditos.filter(creditosItem => {
        const creditosIdentifier = getCreditosIdentifier(creditosItem);
        if (creditosIdentifier == null || creditosCollectionIdentifiers.includes(creditosIdentifier)) {
          return false;
        }
        creditosCollectionIdentifiers.push(creditosIdentifier);
        return true;
      });
      return [...creditosToAdd, ...creditosCollection];
    }
    return creditosCollection;
  }

  protected convertDateFromClient(creditos: ICreditos): ICreditos {
    return Object.assign({}, creditos, {
      tomado: creditos.tomado?.isValid() ? creditos.tomado.format(DATE_FORMAT) : undefined,
      inicioPago: creditos.inicioPago?.isValid() ? creditos.inicioPago.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.tomado = res.body.tomado ? dayjs(res.body.tomado) : undefined;
      res.body.inicioPago = res.body.inicioPago ? dayjs(res.body.inicioPago) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((creditos: ICreditos) => {
        creditos.tomado = creditos.tomado ? dayjs(creditos.tomado) : undefined;
        creditos.inicioPago = creditos.inicioPago ? dayjs(creditos.inicioPago) : undefined;
      });
    }
    return res;
  }
}
