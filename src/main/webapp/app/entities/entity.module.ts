import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CampusiotDeviceModule } from './device/device.module';
import { CampusiotMessageModule } from './message/message.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        CampusiotDeviceModule,
        CampusiotMessageModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CampusiotEntityModule {}
