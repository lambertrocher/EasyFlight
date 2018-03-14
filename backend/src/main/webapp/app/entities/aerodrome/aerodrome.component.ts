import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Aerodrome } from './aerodrome.model';
import { AerodromeService } from './aerodrome.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-aerodrome',
    templateUrl: './aerodrome.component.html'
})
export class AerodromeComponent implements OnInit, OnDestroy {
aerodromes: Aerodrome[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private aerodromeService: AerodromeService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.aerodromeService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<Aerodrome[]>) => this.aerodromes = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.aerodromeService.query().subscribe(
            (res: HttpResponse<Aerodrome[]>) => {
                this.aerodromes = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInAerodromes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Aerodrome) {
        return item.id;
    }
    registerChangeInAerodromes() {
        this.eventSubscriber = this.eventManager.subscribe('aerodromeListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
