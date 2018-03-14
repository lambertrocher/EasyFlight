import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Piste } from './piste.model';
import { PistePopupService } from './piste-popup.service';
import { PisteService } from './piste.service';

@Component({
    selector: 'jhi-piste-dialog',
    templateUrl: './piste-dialog.component.html'
})
export class PisteDialogComponent implements OnInit {

    piste: Piste;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private pisteService: PisteService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.piste.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pisteService.update(this.piste));
        } else {
            this.subscribeToSaveResponse(
                this.pisteService.create(this.piste));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Piste>>) {
        result.subscribe((res: HttpResponse<Piste>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Piste) {
        this.eventManager.broadcast({ name: 'pisteListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-piste-popup',
    template: ''
})
export class PistePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pistePopupService: PistePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pistePopupService
                    .open(PisteDialogComponent as Component, params['id']);
            } else {
                this.pistePopupService
                    .open(PisteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
