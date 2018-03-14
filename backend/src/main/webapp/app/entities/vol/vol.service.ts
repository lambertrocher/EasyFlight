import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Vol } from './vol.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Vol>;

@Injectable()
export class VolService {

    private resourceUrl =  SERVER_API_URL + 'api/vols';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/vols';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(vol: Vol): Observable<EntityResponseType> {
        const copy = this.convert(vol);
        return this.http.post<Vol>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(vol: Vol): Observable<EntityResponseType> {
        const copy = this.convert(vol);
        return this.http.put<Vol>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Vol>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Vol[]>> {
        const options = createRequestOption(req);
        return this.http.get<Vol[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Vol[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Vol[]>> {
        const options = createRequestOption(req);
        return this.http.get<Vol[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Vol[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Vol = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Vol[]>): HttpResponse<Vol[]> {
        const jsonResponse: Vol[] = res.body;
        const body: Vol[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Vol.
     */
    private convertItemFromServer(vol: Vol): Vol {
        const copy: Vol = Object.assign({}, vol);
        copy.dateVol = this.dateUtils
            .convertLocalDateFromServer(vol.dateVol);
        return copy;
    }

    /**
     * Convert a Vol to a JSON which can be sent to the server.
     */
    private convert(vol: Vol): Vol {
        const copy: Vol = Object.assign({}, vol);
        copy.dateVol = this.dateUtils
            .convertLocalDateToServer(vol.dateVol);
        return copy;
    }
}
