import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDevice } from 'app/shared/model/device.model';

type EntityResponseType = HttpResponse<IDevice>;
type EntityArrayResponseType = HttpResponse<IDevice[]>;

@Injectable({ providedIn: 'root' })
export class DeviceService {
    public resourceUrl = SERVER_API_URL + 'api/devices';

    constructor(private http: HttpClient) {}

    create(device: IDevice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(device);
        return this.http
            .post<IDevice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(device: IDevice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(device);
        return this.http
            .put<IDevice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDevice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(device: IDevice): IDevice {
        const copy: IDevice = Object.assign({}, device, {
            createdAt: device.createdAt != null && device.createdAt.isValid() ? device.createdAt.toJSON() : null,
            registeredAt: device.registeredAt != null && device.registeredAt.isValid() ? device.registeredAt.toJSON() : null,
            lastMessageAt: device.lastMessageAt != null && device.lastMessageAt.isValid() ? device.lastMessageAt.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
        res.body.registeredAt = res.body.registeredAt != null ? moment(res.body.registeredAt) : null;
        res.body.lastMessageAt = res.body.lastMessageAt != null ? moment(res.body.lastMessageAt) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((device: IDevice) => {
            device.createdAt = device.createdAt != null ? moment(device.createdAt) : null;
            device.registeredAt = device.registeredAt != null ? moment(device.registeredAt) : null;
            device.lastMessageAt = device.lastMessageAt != null ? moment(device.lastMessageAt) : null;
        });
        return res;
    }
}
