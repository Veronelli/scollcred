import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { IDependencia } from 'app/entities/dependencia/dependencia.model';
import { DependenciaService } from 'app/entities/dependencia/service/dependencia.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;

  dependenciasSharedCollection: IDependencia[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(4)]],
    apellido: [null, [Validators.required, Validators.minLength(4)]],
    telefono: [null, [Validators.minLength(10)]],
    dni: [null, [Validators.minLength(8)]],
    dependencia: [],
  });

  constructor(
    protected clienteService: ClienteService,
    protected dependenciaService: DependenciaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.updateForm(cliente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  trackDependenciaById(index: number, item: IDependencia): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      telefono: cliente.telefono,
      dni: cliente.dni,
      dependencia: cliente.dependencia,
    });

    this.dependenciasSharedCollection = this.dependenciaService.addDependenciaToCollectionIfMissing(
      this.dependenciasSharedCollection,
      cliente.dependencia
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dependenciaService
      .query()
      .pipe(map((res: HttpResponse<IDependencia[]>) => res.body ?? []))
      .pipe(
        map((dependencias: IDependencia[]) =>
          this.dependenciaService.addDependenciaToCollectionIfMissing(dependencias, this.editForm.get('dependencia')!.value)
        )
      )
      .subscribe((dependencias: IDependencia[]) => (this.dependenciasSharedCollection = dependencias));
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      dependencia: this.editForm.get(['dependencia'])!.value,
    };
  }
}
