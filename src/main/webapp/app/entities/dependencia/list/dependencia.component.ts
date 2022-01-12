import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDependencia } from '../dependencia.model';
import { DependenciaService } from '../service/dependencia.service';
import { DependenciaDeleteDialogComponent } from '../delete/dependencia-delete-dialog.component';

@Component({
  selector: 'jhi-dependencia',
  templateUrl: './dependencia.component.html',
})
export class DependenciaComponent implements OnInit {
  dependencias?: IDependencia[];
  isLoading = false;

  constructor(protected dependenciaService: DependenciaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dependenciaService.query().subscribe(
      (res: HttpResponse<IDependencia[]>) => {
        this.isLoading = false;
        this.dependencias = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDependencia): number {
    return item.id!;
  }

  delete(dependencia: IDependencia): void {
    const modalRef = this.modalService.open(DependenciaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dependencia = dependencia;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
