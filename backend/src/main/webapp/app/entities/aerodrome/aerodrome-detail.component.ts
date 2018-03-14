import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Aerodrome } from './aerodrome.model';
import { AerodromeService } from './aerodrome.service';

@Component({
    selector: 'jhi-aerodrome-detail',
    templateUrl: './aerodrome-detail.component.html'
})
export class AerodromeDetailComponent implements OnInit, OnDestroy {

    aerodrome: Aerodrome;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private aerodromeService: AerodromeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAerodromes();
    }

    load(id) {
        this.aerodromeService.find(id)
            .subscribe((aerodromeResponse: HttpResponse<Aerodrome>) => {
                this.aerodrome = aerodromeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAerodromes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'aerodromeListModification',
            (response) => this.load(this.aerodrome.id)
        );
    }
}
