/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DeviceService } from 'app/entities/device/device.service';
import { IDevice, Device, DeviceModel, Network, MessageType } from 'app/shared/model/device.model';

describe('Service Tests', () => {
    describe('Device Service', () => {
        let injector: TestBed;
        let service: DeviceService;
        let httpMock: HttpTestingController;
        let elemDefault: IDevice;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(DeviceService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Device(
                0,
                'AAAAAAA',
                'AAAAAAA',
                DeviceModel.SICONIA,
                Network.CAMPUSIOT,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                0,
                currentDate,
                currentDate,
                currentDate,
                MessageType.JOIN,
                0,
                0,
                0,
                0,
                0
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        createdAt: currentDate.format(DATE_TIME_FORMAT),
                        registeredAt: currentDate.format(DATE_TIME_FORMAT),
                        lastMessageAt: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a Device', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        createdAt: currentDate.format(DATE_TIME_FORMAT),
                        registeredAt: currentDate.format(DATE_TIME_FORMAT),
                        lastMessageAt: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        createdAt: currentDate,
                        registeredAt: currentDate,
                        lastMessageAt: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Device(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Device', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        location: 'BBBBBB',
                        model: 'BBBBBB',
                        network: 'BBBBBB',
                        deveui: 'BBBBBB',
                        appeui: 'BBBBBB',
                        appkey: 'BBBBBB',
                        delayBeforeOffline: 1,
                        createdAt: currentDate.format(DATE_TIME_FORMAT),
                        registeredAt: currentDate.format(DATE_TIME_FORMAT),
                        lastMessageAt: currentDate.format(DATE_TIME_FORMAT),
                        type: 'BBBBBB',
                        fcnt: 1,
                        latitude: 1,
                        longitude: 1,
                        altitude: 1,
                        batteryLevel: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        createdAt: currentDate,
                        registeredAt: currentDate,
                        lastMessageAt: currentDate
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

            it('should return a list of Device', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        location: 'BBBBBB',
                        model: 'BBBBBB',
                        network: 'BBBBBB',
                        deveui: 'BBBBBB',
                        appeui: 'BBBBBB',
                        appkey: 'BBBBBB',
                        delayBeforeOffline: 1,
                        createdAt: currentDate.format(DATE_TIME_FORMAT),
                        registeredAt: currentDate.format(DATE_TIME_FORMAT),
                        lastMessageAt: currentDate.format(DATE_TIME_FORMAT),
                        type: 'BBBBBB',
                        fcnt: 1,
                        latitude: 1,
                        longitude: 1,
                        altitude: 1,
                        batteryLevel: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        createdAt: currentDate,
                        registeredAt: currentDate,
                        lastMessageAt: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(take(1), map(resp => resp.body))
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Device', async () => {
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
