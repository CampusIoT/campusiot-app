import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-device-update',
    templateUrl: './device-update.component.html'
})
export class DeviceUpdateComponent implements OnInit {
    device: IDevice;
    isSaving: boolean;

    users: IUser[];
    createdAt: string;
    registeredAt: string;
    lastMessageAt: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private deviceService: DeviceService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ device }) => {
            this.device = device;
            this.createdAt = this.device.createdAt != null ? this.device.createdAt.format(DATE_TIME_FORMAT) : null;
            this.registeredAt = this.device.registeredAt != null ? this.device.registeredAt.format(DATE_TIME_FORMAT) : null;
            this.lastMessageAt = this.device.lastMessageAt != null ? this.device.lastMessageAt.format(DATE_TIME_FORMAT) : null;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.device.createdAt = this.createdAt != null ? moment(this.createdAt, DATE_TIME_FORMAT) : null;
        this.device.registeredAt = this.registeredAt != null ? moment(this.registeredAt, DATE_TIME_FORMAT) : null;
        this.device.lastMessageAt = this.lastMessageAt != null ? moment(this.lastMessageAt, DATE_TIME_FORMAT) : null;
        if (this.device.id !== undefined) {
            this.subscribeToSaveResponse(this.deviceService.update(this.device));
        } else {
            this.subscribeToSaveResponse(this.deviceService.create(this.device));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>) {
        result.subscribe((res: HttpResponse<IDevice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
