import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDependencia, Dependencia } from '../dependencia.model';

import { DependenciaService } from './dependencia.service';

describe('Dependencia Service', () => {
  let service: DependenciaService;
  let httpMock: HttpTestingController;
  let elemDefault: IDependencia;
  let expectedResult: IDependencia | IDependencia[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DependenciaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      empleador: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Dependencia', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Dependencia()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dependencia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          empleador: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dependencia', () => {
      const patchObject = Object.assign(
        {
          empleador: 'BBBBBB',
        },
        new Dependencia()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dependencia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          empleador: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Dependencia', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDependenciaToCollectionIfMissing', () => {
      it('should add a Dependencia to an empty array', () => {
        const dependencia: IDependencia = { id: 123 };
        expectedResult = service.addDependenciaToCollectionIfMissing([], dependencia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dependencia);
      });

      it('should not add a Dependencia to an array that contains it', () => {
        const dependencia: IDependencia = { id: 123 };
        const dependenciaCollection: IDependencia[] = [
          {
            ...dependencia,
          },
          { id: 456 },
        ];
        expectedResult = service.addDependenciaToCollectionIfMissing(dependenciaCollection, dependencia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dependencia to an array that doesn't contain it", () => {
        const dependencia: IDependencia = { id: 123 };
        const dependenciaCollection: IDependencia[] = [{ id: 456 }];
        expectedResult = service.addDependenciaToCollectionIfMissing(dependenciaCollection, dependencia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependencia);
      });

      it('should add only unique Dependencia to an array', () => {
        const dependenciaArray: IDependencia[] = [{ id: 123 }, { id: 456 }, { id: 23723 }];
        const dependenciaCollection: IDependencia[] = [{ id: 123 }];
        expectedResult = service.addDependenciaToCollectionIfMissing(dependenciaCollection, ...dependenciaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dependencia: IDependencia = { id: 123 };
        const dependencia2: IDependencia = { id: 456 };
        expectedResult = service.addDependenciaToCollectionIfMissing([], dependencia, dependencia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependencia);
        expect(expectedResult).toContain(dependencia2);
      });

      it('should accept null and undefined values', () => {
        const dependencia: IDependencia = { id: 123 };
        expectedResult = service.addDependenciaToCollectionIfMissing([], null, dependencia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dependencia);
      });

      it('should return initial array if no Dependencia is added', () => {
        const dependenciaCollection: IDependencia[] = [{ id: 123 }];
        expectedResult = service.addDependenciaToCollectionIfMissing(dependenciaCollection, undefined, null);
        expect(expectedResult).toEqual(dependenciaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
