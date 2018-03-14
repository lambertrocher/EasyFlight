import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Avion } from './avion.model';
import { AvionService } from './avion.service';

@Component({
    selector: 'jhi-avion-detail',
    templateUrl: './avion-detail.component.html'
})
export class AvionDetailComponent implements OnInit, OnDestroy {

    avion: Avion;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private avionService: AvionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAvions();
    }

    load(id) {
        this.avionService.find(id)
            .subscribe((avionResponse: HttpResponse<Avion>) => {
                this.avion = avionResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAvions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'avionListModification',
            (response) => this.load(this.avion.id)
        );
    }
}
