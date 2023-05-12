import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMasterLookup } from '../master-lookup.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../master-lookup.test-samples';

import { MasterLookupService, RestMasterLookup } from './master-lookup.service';

const requireRestSample: RestMasterLookup = {
  ...sampleWithRequiredData,
  lastModified: sampleWithRequiredData.lastModified?.toJSON(),
};

describe('MasterLookup Service', () => {
  let service: MasterLookupService;
  let httpMock: HttpTestingController;
  let expectedResult: IMasterLookup | IMasterLookup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MasterLookupService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MasterLookup', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const masterLookup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(masterLookup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MasterLookup', () => {
      const masterLookup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(masterLookup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MasterLookup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MasterLookup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MasterLookup', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMasterLookupToCollectionIfMissing', () => {
      it('should add a MasterLookup to an empty array', () => {
        const masterLookup: IMasterLookup = sampleWithRequiredData;
        expectedResult = service.addMasterLookupToCollectionIfMissing([], masterLookup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(masterLookup);
      });

      it('should not add a MasterLookup to an array that contains it', () => {
        const masterLookup: IMasterLookup = sampleWithRequiredData;
        const masterLookupCollection: IMasterLookup[] = [
          {
            ...masterLookup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMasterLookupToCollectionIfMissing(masterLookupCollection, masterLookup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MasterLookup to an array that doesn't contain it", () => {
        const masterLookup: IMasterLookup = sampleWithRequiredData;
        const masterLookupCollection: IMasterLookup[] = [sampleWithPartialData];
        expectedResult = service.addMasterLookupToCollectionIfMissing(masterLookupCollection, masterLookup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(masterLookup);
      });

      it('should add only unique MasterLookup to an array', () => {
        const masterLookupArray: IMasterLookup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const masterLookupCollection: IMasterLookup[] = [sampleWithRequiredData];
        expectedResult = service.addMasterLookupToCollectionIfMissing(masterLookupCollection, ...masterLookupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const masterLookup: IMasterLookup = sampleWithRequiredData;
        const masterLookup2: IMasterLookup = sampleWithPartialData;
        expectedResult = service.addMasterLookupToCollectionIfMissing([], masterLookup, masterLookup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(masterLookup);
        expect(expectedResult).toContain(masterLookup2);
      });

      it('should accept null and undefined values', () => {
        const masterLookup: IMasterLookup = sampleWithRequiredData;
        expectedResult = service.addMasterLookupToCollectionIfMissing([], null, masterLookup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(masterLookup);
      });

      it('should return initial array if no MasterLookup is added', () => {
        const masterLookupCollection: IMasterLookup[] = [sampleWithRequiredData];
        expectedResult = service.addMasterLookupToCollectionIfMissing(masterLookupCollection, undefined, null);
        expect(expectedResult).toEqual(masterLookupCollection);
      });
    });

    describe('compareMasterLookup', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMasterLookup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMasterLookup(entity1, entity2);
        const compareResult2 = service.compareMasterLookup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMasterLookup(entity1, entity2);
        const compareResult2 = service.compareMasterLookup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMasterLookup(entity1, entity2);
        const compareResult2 = service.compareMasterLookup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
