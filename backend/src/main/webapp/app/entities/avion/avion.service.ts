import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Avion } from './avion.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Avion>;

@Injectable()
export class AvionService {

    private resourceUrl =  SERVER_API_URL + 'api/avions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/avions';

    constructor(private http: HttpClient) { }

    create(avion: Avion): Observable<EntityResponseType> {
        const copy = this.convert(avion);
        return this.http.post<Avion>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(avion: Avion): Observable<EntityResponseType> {
        const copy = this.convert(avion);
        return this.http.put<Avion>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Avion>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Avion[]>> {
        const options = createRequestOption(req);
        return this.http.get<Avion[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Avion[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Avion[]>> {
        const options = createRequestOption(req);
        return this.http.get<Avion[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Avion[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Avion = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Avion[]>): HttpResponse<Avion[]> {
        const jsonResponse: Avion[] = res.body;
        const body: Avion[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Avion.
     */
    private convertItemFromServer(avion: Avion): Avion {
        const copy: Avion = Object.assign({}, avion);
        return copy;
    }

    /**
     * Convert a Avion to a JSON which can be sent to the server.
     */
    private convert(avion: Avion): Avion {
        const copy: Avion = Object.assign({}, avion);
        return copy;
    }
}
