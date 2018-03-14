import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Avion } from './avion.model';
import { AvionPopupService } from './avion-popup.service';
import { AvionService } from './avion.service';
import { Reservoir, ReservoirService } from '../reservoir';
import { Maintenance, MaintenanceService } from '../maintenance';

@Component({
    selector: 'jhi-avion-dialog',
    templateUrl: './avion-dialog.component.html'
})
export class AvionDialogComponent implements OnInit {

    avion: Avion;
    isSaving: boolean;

    reservoirs: Reservoir[];

    maintenances: Maintenance[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private avionService: AvionService,
        private reservoirService: ReservoirService,
        private maintenanceService: MaintenanceService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.reservoirService.query()
            .subscribe((res: HttpResponse<Reservoir[]>) => { this.reservoirs = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.maintenanceService.query()
            .subscribe((res: HttpResponse<Maintenance[]>) => { this.maintenances = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.avion.id !== undefined) {
            this.subscribeToSaveResponse(
                this.avionService.update(this.avion));
        } else {
            this.subscribeToSaveResponse(
                this.avionService.create(this.avion));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Avion>>) {
        result.subscribe((res: HttpResponse<Avion>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Avion) {
        this.eventManager.broadcast({ name: 'avionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackReservoirById(index: number, item: Reservoir) {
        return item.id;
    }

    trackMaintenanceById(index: number, item: Maintenance) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-avion-popup',
    template: ''
})
export class AvionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private avionPopupService: AvionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.avionPopupService
                    .open(AvionDialogComponent as Component, params['id']);
            } else {
                this.avionPopupService
                    .open(AvionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
