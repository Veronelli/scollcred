import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMutual, Mutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';

@Component({
  selector: 'jhi-mutual-update',
  templateUrl: './mutual-update.component.html',
})
export class MutualUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(6)]],
  });

  constructor(protected mutualService: MutualService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mutual }) => {
      this.updateForm(mutual);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mutual = this.createFromForm();
    if (mutual.id !== undefined) {
      this.subscribeToSaveResponse(this.mutualService.update(mutual));
    } else {
      this.subscribeToSaveResponse(this.mutualService.create(mutual));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMutual>>): void {
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

  protected updateForm(mutual: IMutual): void {
    this.editForm.patchValue({
      id: mutual.id,
      nombre: mutual.nombre,
    });
  }

  protected createFromForm(): IMutual {
    return {
      ...new Mutual(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
    };
  }
}
