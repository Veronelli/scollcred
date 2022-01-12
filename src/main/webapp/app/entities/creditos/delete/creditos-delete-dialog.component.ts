import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICreditos } from '../creditos.model';
import { CreditosService } from '../service/creditos.service';

@Component({
  templateUrl: './creditos-delete-dialog.component.html',
})
export class CreditosDeleteDialogComponent {
  creditos?: ICreditos;

  constructor(protected creditosService: CreditosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.creditosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
