import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MasterLookupDetailComponent } from './master-lookup-detail.component';

describe('MasterLookup Management Detail Component', () => {
  let comp: MasterLookupDetailComponent;
  let fixture: ComponentFixture<MasterLookupDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MasterLookupDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ masterLookup: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MasterLookupDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MasterLookupDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load masterLookup on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.masterLookup).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
