import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Piste } from './piste.model';
import { PisteService } from './piste.service';

@Component({
    selector: 'jhi-piste-detail',
    templateUrl: './piste-detail.component.html'
})
export class PisteDetailComponent implements OnInit, OnDestroy {

    piste: Piste;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pisteService: PisteService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPistes();
    }

    load(id) {
        this.pisteService.find(id)
            .subscribe((pisteResponse: HttpResponse<Piste>) => {
                this.piste = pisteResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPistes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pisteListModification',
            (response) => this.load(this.piste.id)
        );
    }
}
