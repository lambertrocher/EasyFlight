import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Aerodrome } from './aerodrome.model';
import { AerodromePopupService } from './aerodrome-popup.service';
import { AerodromeService } from './aerodrome.service';
import { Piste, PisteService } from '../piste';

@Component({
    selector: 'jhi-aerodrome-dialog',
    templateUrl: './aerodrome-dialog.component.html'
})
export class AerodromeDialogComponent implements OnInit {

    aerodrome: Aerodrome;
    isSaving: boolean;

    pistes: Piste[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private aerodromeService: AerodromeService,
        private pisteService: PisteService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.pisteService.query()
            .subscribe((res: HttpResponse<Piste[]>) => { this.pistes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.aerodrome.id !== undefined) {
            this.subscribeToSaveResponse(
                this.aerodromeService.update(this.aerodrome));
        } else {
            this.subscribeToSaveResponse(
                this.aerodromeService.create(this.aerodrome));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Aerodrome>>) {
        result.subscribe((res: HttpResponse<Aerodrome>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Aerodrome) {
        this.eventManager.broadcast({ name: 'aerodromeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPisteById(index: number, item: Piste) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-aerodrome-popup',
    template: ''
})
export class AerodromePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private aerodromePopupService: AerodromePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.aerodromePopupService
                    .open(AerodromeDialogComponent as Component, params['id']);
            } else {
                this.aerodromePopupService
                    .open(AerodromeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
