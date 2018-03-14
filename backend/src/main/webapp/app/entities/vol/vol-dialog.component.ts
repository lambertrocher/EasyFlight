import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Vol } from './vol.model';
import { VolPopupService } from './vol-popup.service';
import { VolService } from './vol.service';
import { Avion, AvionService } from '../avion';
import { Pilote, PiloteService } from '../pilote';
import { Aerodrome, AerodromeService } from '../aerodrome';

@Component({
    selector: 'jhi-vol-dialog',
    templateUrl: './vol-dialog.component.html'
})
export class VolDialogComponent implements OnInit {

    vol: Vol;
    isSaving: boolean;

    avions: Avion[];

    pilotes: Pilote[];

    aerodromes: Aerodrome[];
    dateVolDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private volService: VolService,
        private avionService: AvionService,
        private piloteService: PiloteService,
        private aerodromeService: AerodromeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.avionService.query()
            .subscribe((res: HttpResponse<Avion[]>) => { this.avions = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.piloteService.query()
            .subscribe((res: HttpResponse<Pilote[]>) => { this.pilotes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.aerodromeService.query()
            .subscribe((res: HttpResponse<Aerodrome[]>) => { this.aerodromes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.vol.id !== undefined) {
            this.subscribeToSaveResponse(
                this.volService.update(this.vol));
        } else {
            this.subscribeToSaveResponse(
                this.volService.create(this.vol));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Vol>>) {
        result.subscribe((res: HttpResponse<Vol>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Vol) {
        this.eventManager.broadcast({ name: 'volListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAvionById(index: number, item: Avion) {
        return item.id;
    }

    trackPiloteById(index: number, item: Pilote) {
        return item.id;
    }

    trackAerodromeById(index: number, item: Aerodrome) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-vol-popup',
    template: ''
})
export class VolPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private volPopupService: VolPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.volPopupService
                    .open(VolDialogComponent as Component, params['id']);
            } else {
                this.volPopupService
                    .open(VolDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
