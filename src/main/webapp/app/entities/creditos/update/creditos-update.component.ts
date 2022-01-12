import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICreditos, Creditos } from '../creditos.model';
import { CreditosService } from '../service/creditos.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IMutual } from 'app/entities/mutual/mutual.model';
import { MutualService } from 'app/entities/mutual/service/mutual.service';

@Component({
  selector: 'jhi-creditos-update',
  templateUrl: './creditos-update.component.html',
})
export class CreditosUpdateComponent implements OnInit {
  isSaving = false;

  clientesSharedCollection: ICliente[] = [];
  mutualsSharedCollection: IMutual[] = [];

  editForm = this.fb.group({
    id: [],
    emisionCuotas: [null, [Validators.required]],
    monto: [null, [Validators.required]],
    pagoCuota: [null, [Validators.required]],
    cantidadCuotas: [null, [Validators.required]],
    tomado: [null, [Validators.required]],
    inicioPago: [null, [Validators.required]],
    cliente: [null, Validators.required],
    mutual: [null, Validators.required],
  });

  constructor(
    protected creditosService: CreditosService,
    protected clienteService: ClienteService,
    protected mutualService: MutualService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditos }) => {
      this.updateForm(creditos);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const creditos = this.createFromForm();
    if (creditos.id !== undefined) {
      this.subscribeToSaveResponse(this.creditosService.update(creditos));
    } else {
      this.subscribeToSaveResponse(this.creditosService.create(creditos));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  trackMutualById(index: number, item: IMutual): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICreditos>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(creditos: ICreditos): void {
    this.editForm.patchValue({
      id: creditos.id,
      emisionCuotas: creditos.emisionCuotas,
      monto: creditos.monto,
      pagoCuota: creditos.pagoCuota,
      cantidadCuotas: creditos.cantidadCuotas,
      tomado: creditos.tomado,
      inicioPago: creditos.inicioPago,
      cliente: creditos.cliente,
      mutual: creditos.mutual,
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, creditos.cliente);
    this.mutualsSharedCollection = this.mutualService.addMutualToCollectionIfMissing(this.mutualsSharedCollection, creditos.mutual);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.mutualService
      .query()
      .pipe(map((res: HttpResponse<IMutual[]>) => res.body ?? []))
      .pipe(map((mutuals: IMutual[]) => this.mutualService.addMutualToCollectionIfMissing(mutuals, this.editForm.get('mutual')!.value)))
      .subscribe((mutuals: IMutual[]) => (this.mutualsSharedCollection = mutuals));
  }

  protected createFromForm(): ICreditos {
    return {
      ...new Creditos(),
      id: this.editForm.get(['id'])!.value,
      emisionCuotas: this.editForm.get(['emisionCuotas'])!.value,
      monto: this.editForm.get(['monto'])!.value,
      pagoCuota: this.editForm.get(['pagoCuota'])!.value,
      cantidadCuotas: this.editForm.get(['cantidadCuotas'])!.value,
      tomado: this.editForm.get(['tomado'])!.value,
      inicioPago: this.editForm.get(['inicioPago'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      mutual: this.editForm.get(['mutual'])!.value,
    };
  }
}
