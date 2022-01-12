import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DependenciaService } from '../service/dependencia.service';

import { DependenciaComponent } from './dependencia.component';

describe('Dependencia Management Component', () => {
  let comp: DependenciaComponent;
  let fixture: ComponentFixture<DependenciaComponent>;
  let service: DependenciaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DependenciaComponent],
    })
      .overrideTemplate(DependenciaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DependenciaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DependenciaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.dependencias?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
