/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { HolidayService } from 'app/entities/holiday/holiday.service';
import { IHoliday, Holiday } from 'app/shared/model/holiday.model';

describe('Service Tests', () => {
    describe('Holiday Service', () => {
        let injector: TestBed;
        let service: HolidayService;
        let httpMock: HttpTestingController;
        let elemDefault: IHoliday;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(HolidayService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Holiday(0, currentDate, currentDate, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        fromDate: currentDate.format(DATE_FORMAT),
                        toDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Holiday', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        fromDate: currentDate.format(DATE_FORMAT),
                        toDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        fromDate: currentDate,
                        toDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Holiday(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Holiday', async () => {
                const returnedFromService = Object.assign(
                    {
                        fromDate: currentDate.format(DATE_FORMAT),
                        toDate: currentDate.format(DATE_FORMAT),
                        numberOfDays: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        fromDate: currentDate,
                        toDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Holiday', async () => {
                const returnedFromService = Object.assign(
                    {
                        fromDate: currentDate.format(DATE_FORMAT),
                        toDate: currentDate.format(DATE_FORMAT),
                        numberOfDays: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        fromDate: currentDate,
                        toDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Holiday', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
