import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';
import { MutualDeleteDialogComponent } from '../delete/mutual-delete-dialog.component';

@Component({
  selector: 'jhi-mutual',
  templateUrl: './mutual.component.html',
})
export class MutualComponent implements OnInit {
  mutuals?: IMutual[];
  isLoading = false;

  constructor(protected mutualService: MutualService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.mutualService.query().subscribe(
      (res: HttpResponse<IMutual[]>) => {
        this.isLoading = false;
        this.mutuals = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMutual): number {
    return item.id!;
  }

  delete(mutual: IMutual): void {
    const modalRef = this.modalService.open(MutualDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mutual = mutual;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
