jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MutualService } from '../service/mutual.service';
import { IMutual, Mutual } from '../mutual.model';

import { MutualUpdateComponent } from './mutual-update.component';

describe('Mutual Management Update Component', () => {
  let comp: MutualUpdateComponent;
  let fixture: ComponentFixture<MutualUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mutualService: MutualService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MutualUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MutualUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MutualUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mutualService = TestBed.inject(MutualService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const mutual: IMutual = { id: 456 };

      activatedRoute.data = of({ mutual });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(mutual));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mutual>>();
      const mutual = { id: 123 };
      jest.spyOn(mutualService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mutual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mutual }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(mutualService.update).toHaveBeenCalledWith(mutual);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mutual>>();
      const mutual = new Mutual();
      jest.spyOn(mutualService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mutual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mutual }));
      saveSubject.complete();

      // THEN
      expect(mutualService.create).toHaveBeenCalledWith(mutual);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mutual>>();
      const mutual = { id: 123 };
      jest.spyOn(mutualService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mutual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mutualService.update).toHaveBeenCalledWith(mutual);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
