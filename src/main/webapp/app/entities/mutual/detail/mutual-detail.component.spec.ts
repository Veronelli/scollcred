import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MutualDetailComponent } from './mutual-detail.component';

describe('Mutual Management Detail Component', () => {
  let comp: MutualDetailComponent;
  let fixture: ComponentFixture<MutualDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MutualDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mutual: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MutualDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MutualDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mutual on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mutual).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
