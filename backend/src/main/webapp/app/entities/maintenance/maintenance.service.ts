import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Maintenance } from './maintenance.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Maintenance>;

@Injectable()
export class MaintenanceService {

    private resourceUrl =  SERVER_API_URL + 'api/maintenances';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/maintenances';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(maintenance: Maintenance): Observable<EntityResponseType> {
        const copy = this.convert(maintenance);
        return this.http.post<Maintenance>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(maintenance: Maintenance): Observable<EntityResponseType> {
        const copy = this.convert(maintenance);
        return this.http.put<Maintenance>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Maintenance>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Maintenance[]>> {
        const options = createRequestOption(req);
        return this.http.get<Maintenance[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Maintenance[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Maintenance[]>> {
        const options = createRequestOption(req);
        return this.http.get<Maintenance[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Maintenance[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Maintenance = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Maintenance[]>): HttpResponse<Maintenance[]> {
        const jsonResponse: Maintenance[] = res.body;
        const body: Maintenance[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Maintenance.
     */
    private convertItemFromServer(maintenance: Maintenance): Maintenance {
        const copy: Maintenance = Object.assign({}, maintenance);
        copy.dateMaintenance = this.dateUtils
            .convertLocalDateFromServer(maintenance.dateMaintenance);
        return copy;
    }

    /**
     * Convert a Maintenance to a JSON which can be sent to the server.
     */
    private convert(maintenance: Maintenance): Maintenance {
        const copy: Maintenance = Object.assign({}, maintenance);
        copy.dateMaintenance = this.dateUtils
            .convertLocalDateToServer(maintenance.dateMaintenance);
        return copy;
    }
}
