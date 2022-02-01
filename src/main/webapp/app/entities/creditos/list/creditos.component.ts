import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICreditos, CreditosDTO } from '../creditos.model';
import { CreditosService } from '../service/creditos.service';
import { CreditosDeleteDialogComponent } from '../delete/creditos-delete-dialog.component';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'jhi-creditos',
  templateUrl: './creditos.component.html',
  styleUrls: ['./creditos.component.css'],
})
export class CreditosComponent implements OnInit {
  creditos?: CreditosDTO;
  isLoading = false;
  numPages!: Array<number>;
  numPage = 0;
  filter: any = {
    cliente: '',
    mutual: '',
    page: 0,
  };

  constructor(protected creditosService: CreditosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.creditosService.query(this.filter).subscribe(
      (res: HttpResponse<CreditosDTO>) => {
        this.isLoading = false;
        // eslint-disable-next-line no-console
        console.log('dasdsada', res.body);
        this.creditos = res.body ?? undefined;
        this.setNumberPages();
        this.isLoading = false;
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

  get getModalFilter(): boolean {
    return this.creditosService.getModalFilter;
  }

  toggleModalFilter(): void {
    this.creditosService.setModalFilter(this.getModalFilter);
  }

  updateCreditos(newCreditos: any): void {
    // eslint-disable-next-line no-console
    console.log('fdsfsfsdfdsfs', newCreditos);
    this.creditos = { credtios: newCreditos.credtios, length: newCreditos.length };
    this.filter = { ...newCreditos.filter };
    this.setNumberPages();
  }

  setNumberPages(): void {
    let num = Math.floor(this.creditos!.length / 10);
    if (this.creditos!.length % 10 !== 0) {
      num++;
    }
    this.numPages = Array(num);
  }

  nextPage(pageNum: number): void {
    // eslint-disable-next-line no-console
    console.log(this.filter);
    this.filter.page = pageNum;
    this.numPage = pageNum;
    this.loadAll();
  }
}
