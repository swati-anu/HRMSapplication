import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MasterLookupFormService } from './master-lookup-form.service';
import { MasterLookupService } from '../service/master-lookup.service';
import { IMasterLookup } from '../master-lookup.model';

import { MasterLookupUpdateComponent } from './master-lookup-update.component';

describe('MasterLookup Management Update Component', () => {
  let comp: MasterLookupUpdateComponent;
  let fixture: ComponentFixture<MasterLookupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let masterLookupFormService: MasterLookupFormService;
  let masterLookupService: MasterLookupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MasterLookupUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MasterLookupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MasterLookupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    masterLookupFormService = TestBed.inject(MasterLookupFormService);
    masterLookupService = TestBed.inject(MasterLookupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const masterLookup: IMasterLookup = { id: 456 };

      activatedRoute.data = of({ masterLookup });
      comp.ngOnInit();

      expect(comp.masterLookup).toEqual(masterLookup);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMasterLookup>>();
      const masterLookup = { id: 123 };
      jest.spyOn(masterLookupFormService, 'getMasterLookup').mockReturnValue(masterLookup);
      jest.spyOn(masterLookupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterLookup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: masterLookup }));
      saveSubject.complete();

      // THEN
      expect(masterLookupFormService.getMasterLookup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(masterLookupService.update).toHaveBeenCalledWith(expect.objectContaining(masterLookup));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMasterLookup>>();
      const masterLookup = { id: 123 };
      jest.spyOn(masterLookupFormService, 'getMasterLookup').mockReturnValue({ id: null });
      jest.spyOn(masterLookupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterLookup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: masterLookup }));
      saveSubject.complete();

      // THEN
      expect(masterLookupFormService.getMasterLookup).toHaveBeenCalled();
      expect(masterLookupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMasterLookup>>();
      const masterLookup = { id: 123 };
      jest.spyOn(masterLookupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ masterLookup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(masterLookupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
