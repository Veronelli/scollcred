import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICreditos } from '../creditos.model';

@Component({
  selector: 'jhi-creditos-detail',
  templateUrl: './creditos-detail.component.html',
})
export class CreditosDetailComponent implements OnInit {
  creditos: ICreditos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditos }) => {
      this.creditos = creditos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
