import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMutual, Mutual } from '../mutual.model';

import { MutualService } from './mutual.service';

describe('Mutual Service', () => {
  let service: MutualService;
  let httpMock: HttpTestingController;
  let elemDefault: IMutual;
  let expectedResult: IMutual | IMutual[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MutualService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
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

    it('should create a Mutual', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Mutual()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mutual', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mutual', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
        },
        new Mutual()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mutual', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
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

    it('should delete a Mutual', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMutualToCollectionIfMissing', () => {
      it('should add a Mutual to an empty array', () => {
        const mutual: IMutual = { id: 123 };
        expectedResult = service.addMutualToCollectionIfMissing([], mutual);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mutual);
      });

      it('should not add a Mutual to an array that contains it', () => {
        const mutual: IMutual = { id: 123 };
        const mutualCollection: IMutual[] = [
          {
            ...mutual,
          },
          { id: 456 },
        ];
        expectedResult = service.addMutualToCollectionIfMissing(mutualCollection, mutual);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mutual to an array that doesn't contain it", () => {
        const mutual: IMutual = { id: 123 };
        const mutualCollection: IMutual[] = [{ id: 456 }];
        expectedResult = service.addMutualToCollectionIfMissing(mutualCollection, mutual);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mutual);
      });

      it('should add only unique Mutual to an array', () => {
        const mutualArray: IMutual[] = [{ id: 123 }, { id: 456 }, { id: 13962 }];
        const mutualCollection: IMutual[] = [{ id: 123 }];
        expectedResult = service.addMutualToCollectionIfMissing(mutualCollection, ...mutualArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mutual: IMutual = { id: 123 };
        const mutual2: IMutual = { id: 456 };
        expectedResult = service.addMutualToCollectionIfMissing([], mutual, mutual2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mutual);
        expect(expectedResult).toContain(mutual2);
      });

      it('should accept null and undefined values', () => {
        const mutual: IMutual = { id: 123 };
        expectedResult = service.addMutualToCollectionIfMissing([], null, mutual, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mutual);
      });

      it('should return initial array if no Mutual is added', () => {
        const mutualCollection: IMutual[] = [{ id: 123 }];
        expectedResult = service.addMutualToCollectionIfMissing(mutualCollection, undefined, null);
        expect(expectedResult).toEqual(mutualCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
