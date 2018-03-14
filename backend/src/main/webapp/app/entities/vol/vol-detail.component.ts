import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Vol } from './vol.model';
import { VolService } from './vol.service';

@Component({
    selector: 'jhi-vol-detail',
    templateUrl: './vol-detail.component.html'
})
export class VolDetailComponent implements OnInit, OnDestroy {

    vol: Vol;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private volService: VolService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVols();
    }

    load(id) {
        this.volService.find(id)
            .subscribe((volResponse: HttpResponse<Vol>) => {
                this.vol = volResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVols() {
        this.eventSubscriber = this.eventManager.subscribe(
            'volListModification',
            (response) => this.load(this.vol.id)
        );
    }
}
