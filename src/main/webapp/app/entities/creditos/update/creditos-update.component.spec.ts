jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CreditosService } from '../service/creditos.service';
import { ICreditos, Creditos } from '../creditos.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IMutual } from 'app/entities/mutual/mutual.model';
import { MutualService } from 'app/entities/mutual/service/mutual.service';

import { CreditosUpdateComponent } from './creditos-update.component';

describe('Creditos Management Update Component', () => {
  let comp: CreditosUpdateComponent;
  let fixture: ComponentFixture<CreditosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let creditosService: CreditosService;
  let clienteService: ClienteService;
  let mutualService: MutualService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CreditosUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CreditosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CreditosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    creditosService = TestBed.inject(CreditosService);
    clienteService = TestBed.inject(ClienteService);
    mutualService = TestBed.inject(MutualService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const creditos: ICreditos = { id: 456 };
      const cliente: ICliente = { id: 92442 };
      creditos.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 43022 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ creditos });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, ...additionalClientes);
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Mutual query and add missing value', () => {
      const creditos: ICreditos = { id: 456 };
      const mutual: IMutual = { id: 44294 };
      creditos.mutual = mutual;

      const mutualCollection: IMutual[] = [{ id: 75974 }];
      jest.spyOn(mutualService, 'query').mockReturnValue(of(new HttpResponse({ body: mutualCollection })));
      const additionalMutuals = [mutual];
      const expectedCollection: IMutual[] = [...additionalMutuals, ...mutualCollection];
      jest.spyOn(mutualService, 'addMutualToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ creditos });
      comp.ngOnInit();

      expect(mutualService.query).toHaveBeenCalled();
      expect(mutualService.addMutualToCollectionIfMissing).toHaveBeenCalledWith(mutualCollection, ...additionalMutuals);
      expect(comp.mutualsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const creditos: ICreditos = { id: 456 };
      const cliente: ICliente = { id: 76147 };
      creditos.cliente = cliente;
      const mutual: IMutual = { id: 33064 };
      creditos.mutual = mutual;

      activatedRoute.data = of({ creditos });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(creditos));
      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.mutualsSharedCollection).toContain(mutual);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Creditos>>();
      const creditos = { id: 123 };
      jest.spyOn(creditosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditos }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(creditosService.update).toHaveBeenCalledWith(creditos);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Creditos>>();
      const creditos = new Creditos();
      jest.spyOn(creditosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditos }));
      saveSubject.complete();

      // THEN
      expect(creditosService.create).toHaveBeenCalledWith(creditos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Creditos>>();
      const creditos = { id: 123 };
      jest.spyOn(creditosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(creditosService.update).toHaveBeenCalledWith(creditos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClienteById', () => {
      it('Should return tracked Cliente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMutualById', () => {
      it('Should return tracked Mutual primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMutualById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
