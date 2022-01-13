import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-head-table',
  templateUrl: './head-table.component.html',
  styleUrls: ['./head-table.component.scss'],
})
export class HeadTableComponent implements OnInit {
  @Input() columnName!: string;
  placeholder = 'Buscar por ';
  ngOnInit(): void {
    this.placeholder += this.columnName;
  }
}
