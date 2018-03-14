import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Reservoir } from './reservoir.model';
import { ReservoirService } from './reservoir.service';

@Component({
    selector: 'jhi-reservoir-detail',
    templateUrl: './reservoir-detail.component.html'
})
export class ReservoirDetailComponent implements OnInit, OnDestroy {

    reservoir: Reservoir;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private reservoirService: ReservoirService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReservoirs();
    }

    load(id) {
        this.reservoirService.find(id)
            .subscribe((reservoirResponse: HttpResponse<Reservoir>) => {
                this.reservoir = reservoirResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReservoirs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'reservoirListModification',
            (response) => this.load(this.reservoir.id)
        );
    }
}
