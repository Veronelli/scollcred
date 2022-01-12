import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DependenciaDetailComponent } from './dependencia-detail.component';

describe('Dependencia Management Detail Component', () => {
  let comp: DependenciaDetailComponent;
  let fixture: ComponentFixture<DependenciaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DependenciaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dependencia: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DependenciaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DependenciaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dependencia on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dependencia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
