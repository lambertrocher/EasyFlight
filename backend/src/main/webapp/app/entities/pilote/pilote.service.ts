import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Pilote } from './pilote.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Pilote>;

@Injectable()
export class PiloteService {

    private resourceUrl =  SERVER_API_URL + 'api/pilotes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/pilotes';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(pilote: Pilote): Observable<EntityResponseType> {
        const copy = this.convert(pilote);
        return this.http.post<Pilote>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(pilote: Pilote): Observable<EntityResponseType> {
        const copy = this.convert(pilote);
        return this.http.put<Pilote>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Pilote>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Pilote[]>> {
        const options = createRequestOption(req);
        return this.http.get<Pilote[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Pilote[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Pilote[]>> {
        const options = createRequestOption(req);
        return this.http.get<Pilote[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Pilote[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Pilote = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Pilote[]>): HttpResponse<Pilote[]> {
        const jsonResponse: Pilote[] = res.body;
        const body: Pilote[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Pilote.
     */
    private convertItemFromServer(pilote: Pilote): Pilote {
        const copy: Pilote = Object.assign({}, pilote);
        copy.dateValiditeLicence = this.dateUtils
            .convertLocalDateFromServer(pilote.dateValiditeLicence);
        return copy;
    }

    /**
     * Convert a Pilote to a JSON which can be sent to the server.
     */
    private convert(pilote: Pilote): Pilote {
        const copy: Pilote = Object.assign({}, pilote);
        copy.dateValiditeLicence = this.dateUtils
            .convertLocalDateToServer(pilote.dateValiditeLicence);
        return copy;
    }
}
