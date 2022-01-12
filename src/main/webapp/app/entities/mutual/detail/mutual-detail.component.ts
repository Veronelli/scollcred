import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMutual } from '../mutual.model';

@Component({
  selector: 'jhi-mutual-detail',
  templateUrl: './mutual-detail.component.html',
})
export class MutualDetailComponent implements OnInit {
  mutual: IMutual | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mutual }) => {
      this.mutual = mutual;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
