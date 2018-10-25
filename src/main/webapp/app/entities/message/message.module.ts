import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CampusiotSharedModule } from 'app/shared';
import { CampusiotAdminModule } from 'app/admin/admin.module';
import {
    MessageComponent,
    MessageDetailComponent,
    MessageUpdateComponent,
    MessageDeletePopupComponent,
    MessageDeleteDialogComponent,
    messageRoute,
    messagePopupRoute
} from './';

const ENTITY_STATES = [...messageRoute, ...messagePopupRoute];

@NgModule({
    imports: [CampusiotSharedModule, CampusiotAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MessageComponent,
        MessageDetailComponent,
        MessageUpdateComponent,
        MessageDeleteDialogComponent,
        MessageDeletePopupComponent
    ],
    entryComponents: [MessageComponent, MessageUpdateComponent, MessageDeleteDialogComponent, MessageDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CampusiotMessageModule {}
