/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CampusiotTestModule } from '../../../test.module';
import { DeviceDeleteDialogComponent } from 'app/entities/device/device-delete-dialog.component';
import { DeviceService } from 'app/entities/device/device.service';

describe('Component Tests', () => {
    describe('Device Management Delete Component', () => {
        let comp: DeviceDeleteDialogComponent;
        let fixture: ComponentFixture<DeviceDeleteDialogComponent>;
        let service: DeviceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampusiotTestModule],
                declarations: [DeviceDeleteDialogComponent]
            })
                .overrideTemplate(DeviceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DeviceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeviceService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
