import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IManager } from 'app/shared/model/manager.model';

type EntityResponseType = HttpResponse<IManager>;
type EntityArrayResponseType = HttpResponse<IManager[]>;

@Injectable({ providedIn: 'root' })
export class ManagerService {
    public resourceUrl = SERVER_API_URL + 'api/managers';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/managers';

    constructor(protected http: HttpClient) {}

    create(manager: IManager): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(manager);
        return this.http
            .post<IManager>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(manager: IManager): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(manager);
        return this.http
            .put<IManager>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IManager>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IManager[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IManager[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(manager: IManager): IManager {
        const copy: IManager = Object.assign({}, manager, {
            modifiedOn: manager.modifiedOn != null && manager.modifiedOn.isValid() ? manager.modifiedOn.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.modifiedOn = res.body.modifiedOn != null ? moment(res.body.modifiedOn) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((manager: IManager) => {
                manager.modifiedOn = manager.modifiedOn != null ? moment(manager.modifiedOn) : null;
            });
        }
        return res;
    }
}
