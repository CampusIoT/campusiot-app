import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDevice } from 'app/shared/model/device.model';

@Component({
    selector: 'jhi-device-detail',
    templateUrl: './device-detail.component.html'
})
export class DeviceDetailComponent implements OnInit {
    device: IDevice;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ device }) => {
            this.device = device;
        });
    }

    previousState() {
        window.history.back();
    }
}
