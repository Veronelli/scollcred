import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CreditosDetailComponent } from './creditos-detail.component';

describe('Creditos Management Detail Component', () => {
  let comp: CreditosDetailComponent;
  let fixture: ComponentFixture<CreditosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreditosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ creditos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CreditosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CreditosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load creditos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.creditos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
