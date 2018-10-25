import { Moment } from 'moment';

export const enum DeviceModel {
    SICONIA = 'SICONIA',
    ADENUIS_TEMP = 'ADENUIS_TEMP',
    ADEUNIS_SENSOR = 'ADEUNIS_SENSOR',
    ASCOEL_IR = 'ASCOEL_IR',
    ASCOEL_PB = 'ASCOEL_PB',
    ELSYS_ERS = 'ELSYS_ERS',
    ELSYS_ELT = 'ELSYS_ELT',
    UNKNOWN = 'UNKNOWN'
}

export const enum Network {
    CAMPUSIOT = 'CAMPUSIOT',
    TTN = 'TTN',
    ORANGE = 'ORANGE',
    OBJETNIOUS = 'OBJETNIOUS'
}

export const enum MessageType {
    JOIN = 'JOIN',
    DTUP = 'DTUP',
    DTDN = 'DTDN'
}

export interface IDevice {
    id?: number;
    name?: string;
    location?: string;
    model?: DeviceModel;
    network?: Network;
    deveui?: string;
    appeui?: string;
    appkey?: string;
    delayBeforeOffline?: number;
    createdAt?: Moment;
    registeredAt?: Moment;
    lastMessageAt?: Moment;
    type?: MessageType;
    fcnt?: number;
    latitude?: number;
    longitude?: number;
    altitude?: number;
    batteryLevel?: number;
    createdByLogin?: string;
    createdById?: number;
}

export class Device implements IDevice {
    constructor(
        public id?: number,
        public name?: string,
        public location?: string,
        public model?: DeviceModel,
        public network?: Network,
        public deveui?: string,
        public appeui?: string,
        public appkey?: string,
        public delayBeforeOffline?: number,
        public createdAt?: Moment,
        public registeredAt?: Moment,
        public lastMessageAt?: Moment,
        public type?: MessageType,
        public fcnt?: number,
        public latitude?: number,
        public longitude?: number,
        public altitude?: number,
        public batteryLevel?: number,
        public createdByLogin?: string,
        public createdById?: number
    ) {}
}
