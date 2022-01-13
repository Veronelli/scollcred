import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICreditos } from '../creditos.model';
import { CreditosService } from '../service/creditos.service';
import { CreditosDeleteDialogComponent } from '../delete/creditos-delete-dialog.component';

@Component({
  selector: 'jhi-creditos',
  templateUrl: './creditos.component.html',
  styleUrls: ['./creditos.component.css'],
})
export class CreditosComponent implements OnInit {
  creditos?: ICreditos[];
  isLoading = false;

  constructor(protected creditosService: CreditosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.creditosService.query().subscribe(
      (res: HttpResponse<ICreditos[]>) => {
        this.isLoading = false;
        this.creditos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICreditos): number {
    return item.id!;
  }

  delete(creditos: ICreditos): void {
    const modalRef = this.modalService.open(CreditosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.creditos = creditos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
