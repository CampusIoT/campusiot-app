import { Moment } from 'moment';

export const enum MessageType {
    JOIN = 'JOIN',
    DTUP = 'DTUP',
    DTDN = 'DTDN'
}

export const enum Network {
    CAMPUSIOT = 'CAMPUSIOT',
    TTN = 'TTN',
    ORANGE = 'ORANGE',
    OBJETNIOUS = 'OBJETNIOUS'
}

export interface IMessage {
    id?: number;
    date?: Moment;
    type?: MessageType;
    fcnt?: number;
    network?: Network;
    payloadContentType?: string;
    payload?: any;
    json?: any;
    latitude?: number;
    longitude?: number;
    altitude?: number;
    batteryLevel?: number;
    sentByDeveui?: string;
    sentById?: number;
    deviceOwnerLogin?: string;
    deviceOwnerId?: number;
}

export class Message implements IMessage {
    constructor(
        public id?: number,
        public date?: Moment,
        public type?: MessageType,
        public fcnt?: number,
        public network?: Network,
        public payloadContentType?: string,
        public payload?: any,
        public json?: any,
        public latitude?: number,
        public longitude?: number,
        public altitude?: number,
        public batteryLevel?: number,
        public sentByDeveui?: string,
        public sentById?: number,
        public deviceOwnerLogin?: string,
        public deviceOwnerId?: number
    ) {}
}
