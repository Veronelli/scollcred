import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDependencia } from '../dependencia.model';
import { DependenciaService } from '../service/dependencia.service';

@Component({
  templateUrl: './dependencia-delete-dialog.component.html',
})
export class DependenciaDeleteDialogComponent {
  dependencia?: IDependencia;

  constructor(protected dependenciaService: DependenciaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dependenciaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
