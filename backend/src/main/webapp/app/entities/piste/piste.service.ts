import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Piste } from './piste.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Piste>;

@Injectable()
export class PisteService {

    private resourceUrl =  SERVER_API_URL + 'api/pistes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/pistes';

    constructor(private http: HttpClient) { }

    create(piste: Piste): Observable<EntityResponseType> {
        const copy = this.convert(piste);
        return this.http.post<Piste>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(piste: Piste): Observable<EntityResponseType> {
        const copy = this.convert(piste);
        return this.http.put<Piste>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Piste>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Piste[]>> {
        const options = createRequestOption(req);
        return this.http.get<Piste[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Piste[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Piste[]>> {
        const options = createRequestOption(req);
        return this.http.get<Piste[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Piste[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Piste = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Piste[]>): HttpResponse<Piste[]> {
        const jsonResponse: Piste[] = res.body;
        const body: Piste[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Piste.
     */
    private convertItemFromServer(piste: Piste): Piste {
        const copy: Piste = Object.assign({}, piste);
        return copy;
    }

    /**
     * Convert a Piste to a JSON which can be sent to the server.
     */
    private convert(piste: Piste): Piste {
        const copy: Piste = Object.assign({}, piste);
        return copy;
    }
}
