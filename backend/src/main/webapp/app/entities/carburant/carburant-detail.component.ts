import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Carburant } from './carburant.model';
import { CarburantService } from './carburant.service';

@Component({
    selector: 'jhi-carburant-detail',
    templateUrl: './carburant-detail.component.html'
})
export class CarburantDetailComponent implements OnInit, OnDestroy {

    carburant: Carburant;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private carburantService: CarburantService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCarburants();
    }

    load(id) {
        this.carburantService.find(id)
            .subscribe((carburantResponse: HttpResponse<Carburant>) => {
                this.carburant = carburantResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCarburants() {
        this.eventSubscriber = this.eventManager.subscribe(
            'carburantListModification',
            (response) => this.load(this.carburant.id)
        );
    }
}
