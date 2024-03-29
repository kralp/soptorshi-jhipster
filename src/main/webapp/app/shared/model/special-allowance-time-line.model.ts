import { Moment } from 'moment';

export const enum AllowanceType {
    HOUSE_RENT = 'HOUSE_RENT',
    MEDICAL_ALLOWANCE = 'MEDICAL_ALLOWANCE',
    FESTIVAL_BONUS = 'FESTIVAL_BONUS',
    OVERTIME_ALLOWANCE = 'OVERTIME_ALLOWANCE',
    OTHER_ALLOWANCE = 'OTHER_ALLOWANCE'
}

export const enum MonthType {
    JANUARY = 'JANUARY',
    FEBRUARY = 'FEBRUARY',
    MARCH = 'MARCH',
    APRIL = 'APRIL',
    MAY = 'MAY',
    JUNE = 'JUNE',
    JULY = 'JULY',
    AUGUST = 'AUGUST',
    SEPTEMBER = 'SEPTEMBER',
    OCTOBER = 'OCTOBER',
    NOVEMBER = 'NOVEMBER',
    DECEMBER = 'DECEMBER'
}

export interface ISpecialAllowanceTimeLine {
    id?: number;
    allowanceType?: AllowanceType;
    year?: number;
    month?: MonthType;
    modifiedBy?: string;
    modifiedOn?: Moment;
}

export class SpecialAllowanceTimeLine implements ISpecialAllowanceTimeLine {
    constructor(
        public id?: number,
        public allowanceType?: AllowanceType,
        public year?: number,
        public month?: MonthType,
        public modifiedBy?: string,
        public modifiedOn?: Moment
    ) {}
}
