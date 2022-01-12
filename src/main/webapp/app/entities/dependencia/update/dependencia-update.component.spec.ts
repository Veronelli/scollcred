jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DependenciaService } from '../service/dependencia.service';
import { IDependencia, Dependencia } from '../dependencia.model';

import { DependenciaUpdateComponent } from './dependencia-update.component';

describe('Dependencia Management Update Component', () => {
  let comp: DependenciaUpdateComponent;
  let fixture: ComponentFixture<DependenciaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dependenciaService: DependenciaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DependenciaUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DependenciaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DependenciaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dependenciaService = TestBed.inject(DependenciaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dependencia: IDependencia = { id: 456 };

      activatedRoute.data = of({ dependencia });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dependencia));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dependencia>>();
      const dependencia = { id: 123 };
      jest.spyOn(dependenciaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependencia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dependencia }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dependenciaService.update).toHaveBeenCalledWith(dependencia);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dependencia>>();
      const dependencia = new Dependencia();
      jest.spyOn(dependenciaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependencia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dependencia }));
      saveSubject.complete();

      // THEN
      expect(dependenciaService.create).toHaveBeenCalledWith(dependencia);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dependencia>>();
      const dependencia = { id: 123 };
      jest.spyOn(dependenciaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependencia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dependenciaService.update).toHaveBeenCalledWith(dependencia);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
