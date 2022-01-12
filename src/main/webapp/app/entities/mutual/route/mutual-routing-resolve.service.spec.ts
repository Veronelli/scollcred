jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMutual, Mutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';

import { MutualRoutingResolveService } from './mutual-routing-resolve.service';

describe('Mutual routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MutualRoutingResolveService;
  let service: MutualService;
  let resultMutual: IMutual | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(MutualRoutingResolveService);
    service = TestBed.inject(MutualService);
    resultMutual = undefined;
  });

  describe('resolve', () => {
    it('should return IMutual returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMutual = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMutual).toEqual({ id: 123 });
    });

    it('should return new IMutual if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMutual = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMutual).toEqual(new Mutual());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Mutual })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMutual = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMutual).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
