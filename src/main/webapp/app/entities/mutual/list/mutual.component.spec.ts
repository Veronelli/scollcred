import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MutualService } from '../service/mutual.service';

import { MutualComponent } from './mutual.component';

describe('Mutual Management Component', () => {
  let comp: MutualComponent;
  let fixture: ComponentFixture<MutualComponent>;
  let service: MutualService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MutualComponent],
    })
      .overrideTemplate(MutualComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MutualComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MutualService);

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
    expect(comp.mutuals?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
