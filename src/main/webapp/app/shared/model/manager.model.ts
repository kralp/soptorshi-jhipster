import { Moment } from 'moment';

export interface IManager {
    id?: number;
    employeeId?: number;
    managerId?: number;
    modifiedBy?: string;
    modifiedOn?: Moment;
}

export class Manager implements IManager {
    constructor(
        public id?: number,
        public employeeId?: number,
        public managerId?: number,
        public modifiedBy?: string,
        public modifiedOn?: Moment
    ) {}
}
