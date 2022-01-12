import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';

@Component({
  templateUrl: './mutual-delete-dialog.component.html',
})
export class MutualDeleteDialogComponent {
  mutual?: IMutual;

  constructor(protected mutualService: MutualService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mutualService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
