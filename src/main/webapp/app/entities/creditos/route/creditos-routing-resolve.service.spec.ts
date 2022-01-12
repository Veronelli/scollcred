jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICreditos, Creditos } from '../creditos.model';
import { CreditosService } from '../service/creditos.service';

import { CreditosRoutingResolveService } from './creditos-routing-resolve.service';

describe('Creditos routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CreditosRoutingResolveService;
  let service: CreditosService;
  let resultCreditos: ICreditos | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CreditosRoutingResolveService);
    service = TestBed.inject(CreditosService);
    resultCreditos = undefined;
  });

  describe('resolve', () => {
    it('should return ICreditos returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCreditos).toEqual({ id: 123 });
    });

    it('should return new ICreditos if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditos = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCreditos).toEqual(new Creditos());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Creditos })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCreditos).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
