import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICreditos, Creditos } from '../creditos.model';

import { CreditosService } from './creditos.service';

describe('Creditos Service', () => {
  let service: CreditosService;
  let httpMock: HttpTestingController;
  let elemDefault: ICreditos;
  let expectedResult: ICreditos | ICreditos[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CreditosService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      emisionCuotas: 0,
      monto: 0,
      pagoCuota: 0,
      cantidadCuotas: 0,
      tomado: currentDate,
      inicioPago: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          tomado: currentDate.format(DATE_FORMAT),
          inicioPago: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Creditos', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          tomado: currentDate.format(DATE_FORMAT),
          inicioPago: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          tomado: currentDate,
          inicioPago: currentDate,
        },
        returnedFromService
      );

      service.create(new Creditos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Creditos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          emisionCuotas: 1,
          monto: 1,
          pagoCuota: 1,
          cantidadCuotas: 1,
          tomado: currentDate.format(DATE_FORMAT),
          inicioPago: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          tomado: currentDate,
          inicioPago: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Creditos', () => {
      const patchObject = Object.assign(
        {
          emisionCuotas: 1,
          monto: 1,
          tomado: currentDate.format(DATE_FORMAT),
          inicioPago: currentDate.format(DATE_FORMAT),
        },
        new Creditos()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          tomado: currentDate,
          inicioPago: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Creditos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          emisionCuotas: 1,
          monto: 1,
          pagoCuota: 1,
          cantidadCuotas: 1,
          tomado: currentDate.format(DATE_FORMAT),
          inicioPago: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          tomado: currentDate,
          inicioPago: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Creditos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCreditosToCollectionIfMissing', () => {
      it('should add a Creditos to an empty array', () => {
        const creditos: ICreditos = { id: 123 };
        expectedResult = service.addCreditosToCollectionIfMissing([], creditos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditos);
      });

      it('should not add a Creditos to an array that contains it', () => {
        const creditos: ICreditos = { id: 123 };
        const creditosCollection: ICreditos[] = [
          {
            ...creditos,
          },
          { id: 456 },
        ];
        expectedResult = service.addCreditosToCollectionIfMissing(creditosCollection, creditos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Creditos to an array that doesn't contain it", () => {
        const creditos: ICreditos = { id: 123 };
        const creditosCollection: ICreditos[] = [{ id: 456 }];
        expectedResult = service.addCreditosToCollectionIfMissing(creditosCollection, creditos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditos);
      });

      it('should add only unique Creditos to an array', () => {
        const creditosArray: ICreditos[] = [{ id: 123 }, { id: 456 }, { id: 17377 }];
        const creditosCollection: ICreditos[] = [{ id: 123 }];
        expectedResult = service.addCreditosToCollectionIfMissing(creditosCollection, ...creditosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const creditos: ICreditos = { id: 123 };
        const creditos2: ICreditos = { id: 456 };
        expectedResult = service.addCreditosToCollectionIfMissing([], creditos, creditos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditos);
        expect(expectedResult).toContain(creditos2);
      });

      it('should accept null and undefined values', () => {
        const creditos: ICreditos = { id: 123 };
        expectedResult = service.addCreditosToCollectionIfMissing([], null, creditos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditos);
      });

      it('should return initial array if no Creditos is added', () => {
        const creditosCollection: ICreditos[] = [{ id: 123 }];
        expectedResult = service.addCreditosToCollectionIfMissing(creditosCollection, undefined, null);
        expect(expectedResult).toEqual(creditosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
