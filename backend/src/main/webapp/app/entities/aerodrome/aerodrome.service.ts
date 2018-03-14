import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Aerodrome } from './aerodrome.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Aerodrome>;

@Injectable()
export class AerodromeService {

    private resourceUrl =  SERVER_API_URL + 'api/aerodromes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/aerodromes';

    constructor(private http: HttpClient) { }

    create(aerodrome: Aerodrome): Observable<EntityResponseType> {
        const copy = this.convert(aerodrome);
        return this.http.post<Aerodrome>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(aerodrome: Aerodrome): Observable<EntityResponseType> {
        const copy = this.convert(aerodrome);
        return this.http.put<Aerodrome>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Aerodrome>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Aerodrome[]>> {
        const options = createRequestOption(req);
        return this.http.get<Aerodrome[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Aerodrome[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Aerodrome[]>> {
        const options = createRequestOption(req);
        return this.http.get<Aerodrome[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Aerodrome[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Aerodrome = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Aerodrome[]>): HttpResponse<Aerodrome[]> {
        const jsonResponse: Aerodrome[] = res.body;
        const body: Aerodrome[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Aerodrome.
     */
    private convertItemFromServer(aerodrome: Aerodrome): Aerodrome {
        const copy: Aerodrome = Object.assign({}, aerodrome);
        return copy;
    }

    /**
     * Convert a Aerodrome to a JSON which can be sent to the server.
     */
    private convert(aerodrome: Aerodrome): Aerodrome {
        const copy: Aerodrome = Object.assign({}, aerodrome);
        return copy;
    }
}
