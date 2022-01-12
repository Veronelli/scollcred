import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDependencia, Dependencia } from '../dependencia.model';
import { DependenciaService } from '../service/dependencia.service';

@Component({
  selector: 'jhi-dependencia-update',
  templateUrl: './dependencia-update.component.html',
})
export class DependenciaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    empleador: [null, [Validators.required, Validators.minLength(4)]],
  });

  constructor(protected dependenciaService: DependenciaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dependencia }) => {
      this.updateForm(dependencia);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dependencia = this.createFromForm();
    if (dependencia.id !== undefined) {
      this.subscribeToSaveResponse(this.dependenciaService.update(dependencia));
    } else {
      this.subscribeToSaveResponse(this.dependenciaService.create(dependencia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDependencia>>): void {
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

  protected updateForm(dependencia: IDependencia): void {
    this.editForm.patchValue({
      id: dependencia.id,
      empleador: dependencia.empleador,
    });
  }

  protected createFromForm(): IDependencia {
    return {
      ...new Dependencia(),
      id: this.editForm.get(['id'])!.value,
      empleador: this.editForm.get(['empleador'])!.value,
    };
  }
}
