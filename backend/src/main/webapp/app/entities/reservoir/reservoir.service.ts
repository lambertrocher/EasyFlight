import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Reservoir } from './reservoir.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Reservoir>;

@Injectable()
export class ReservoirService {

    private resourceUrl =  SERVER_API_URL + 'api/reservoirs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/reservoirs';

    constructor(private http: HttpClient) { }

    create(reservoir: Reservoir): Observable<EntityResponseType> {
        const copy = this.convert(reservoir);
        return this.http.post<Reservoir>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(reservoir: Reservoir): Observable<EntityResponseType> {
        const copy = this.convert(reservoir);
        return this.http.put<Reservoir>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Reservoir>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Reservoir[]>> {
        const options = createRequestOption(req);
        return this.http.get<Reservoir[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Reservoir[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Reservoir[]>> {
        const options = createRequestOption(req);
        return this.http.get<Reservoir[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Reservoir[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Reservoir = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Reservoir[]>): HttpResponse<Reservoir[]> {
        const jsonResponse: Reservoir[] = res.body;
        const body: Reservoir[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Reservoir.
     */
    private convertItemFromServer(reservoir: Reservoir): Reservoir {
        const copy: Reservoir = Object.assign({}, reservoir);
        return copy;
    }

    /**
     * Convert a Reservoir to a JSON which can be sent to the server.
     */
    private convert(reservoir: Reservoir): Reservoir {
        const copy: Reservoir = Object.assign({}, reservoir);
        return copy;
    }
}
