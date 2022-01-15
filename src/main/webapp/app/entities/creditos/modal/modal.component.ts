import { Component, EventEmitter, Output } from '@angular/core';
import { CreditosService } from '../service/creditos.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { ICreditos } from '../creditos.model';

@Component({
  selector: 'jhi-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
})
export class ModalComponent {
  myForm: FormGroup = this.fb.group({
    cliente: [],
    mutual: [],
  });

  @Output() creditosFilter = new EventEmitter<ICreditos[]>();

  constructor(private creditosServicio: CreditosService, private fb: FormBuilder) {
    this.myForm.controls['cliente'].setValue('');
    this.myForm.controls['mutual'].setValue('');

    // eslint-disable-next-line no-console
    console.log(this.myForm.value);
  }
  get modalFilter(): boolean {
    return this.creditosServicio.getModalFilter;
  }
  setModalFilter(): void {
    this.creditosServicio.setModalFilter(this.modalFilter);
  }
  queryCretitos(): void {
    // eslint-disable-next-line no-console
    console.log(this.myForm.value);

    this.creditosServicio.query({ ...this.myForm.value }).subscribe((res: HttpResponse<ICreditos[]>) => {
      // eslint-disable-next-line no-console
      console.log(res.body);

      this.creditosFilter.emit(res.body!);
    });
  }
}
