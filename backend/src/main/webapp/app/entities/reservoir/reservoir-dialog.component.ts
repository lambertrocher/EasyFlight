import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Reservoir } from './reservoir.model';
import { ReservoirPopupService } from './reservoir-popup.service';
import { ReservoirService } from './reservoir.service';
import { Carburant, CarburantService } from '../carburant';

@Component({
    selector: 'jhi-reservoir-dialog',
    templateUrl: './reservoir-dialog.component.html'
})
export class ReservoirDialogComponent implements OnInit {

    reservoir: Reservoir;
    isSaving: boolean;

    carburants: Carburant[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private reservoirService: ReservoirService,
        private carburantService: CarburantService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.carburantService.query()
            .subscribe((res: HttpResponse<Carburant[]>) => { this.carburants = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.reservoir.id !== undefined) {
            this.subscribeToSaveResponse(
                this.reservoirService.update(this.reservoir));
        } else {
            this.subscribeToSaveResponse(
                this.reservoirService.create(this.reservoir));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Reservoir>>) {
        result.subscribe((res: HttpResponse<Reservoir>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Reservoir) {
        this.eventManager.broadcast({ name: 'reservoirListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCarburantById(index: number, item: Carburant) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-reservoir-popup',
    template: ''
})
export class ReservoirPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reservoirPopupService: ReservoirPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.reservoirPopupService
                    .open(ReservoirDialogComponent as Component, params['id']);
            } else {
                this.reservoirPopupService
                    .open(ReservoirDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
