import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Carburant } from './carburant.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Carburant>;

@Injectable()
export class CarburantService {

    private resourceUrl =  SERVER_API_URL + 'api/carburants';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/carburants';

    constructor(private http: HttpClient) { }

    create(carburant: Carburant): Observable<EntityResponseType> {
        const copy = this.convert(carburant);
        return this.http.post<Carburant>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(carburant: Carburant): Observable<EntityResponseType> {
        const copy = this.convert(carburant);
        return this.http.put<Carburant>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Carburant>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Carburant[]>> {
        const options = createRequestOption(req);
        return this.http.get<Carburant[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Carburant[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Carburant[]>> {
        const options = createRequestOption(req);
        return this.http.get<Carburant[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Carburant[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Carburant = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Carburant[]>): HttpResponse<Carburant[]> {
        const jsonResponse: Carburant[] = res.body;
        const body: Carburant[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Carburant.
     */
    private convertItemFromServer(carburant: Carburant): Carburant {
        const copy: Carburant = Object.assign({}, carburant);
        return copy;
    }

    /**
     * Convert a Carburant to a JSON which can be sent to the server.
     */
    private convert(carburant: Carburant): Carburant {
        const copy: Carburant = Object.assign({}, carburant);
        return copy;
    }
}
