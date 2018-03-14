import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Pilote } from './pilote.model';
import { PiloteService } from './pilote.service';

@Component({
    selector: 'jhi-pilote-detail',
    templateUrl: './pilote-detail.component.html'
})
export class PiloteDetailComponent implements OnInit, OnDestroy {

    pilote: Pilote;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private piloteService: PiloteService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPilotes();
    }

    load(id) {
        this.piloteService.find(id)
            .subscribe((piloteResponse: HttpResponse<Pilote>) => {
                this.pilote = piloteResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPilotes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'piloteListModification',
            (response) => this.load(this.pilote.id)
        );
    }
}
