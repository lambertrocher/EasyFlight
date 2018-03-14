import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pilote } from './pilote.model';
import { PilotePopupService } from './pilote-popup.service';
import { PiloteService } from './pilote.service';
import { Qualification, QualificationService } from '../qualification';

@Component({
    selector: 'jhi-pilote-dialog',
    templateUrl: './pilote-dialog.component.html'
})
export class PiloteDialogComponent implements OnInit {

    pilote: Pilote;
    isSaving: boolean;

    qualifications: Qualification[];
    dateValiditeLicenceDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private piloteService: PiloteService,
        private qualificationService: QualificationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.qualificationService.query()
            .subscribe((res: HttpResponse<Qualification[]>) => { this.qualifications = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pilote.id !== undefined) {
            this.subscribeToSaveResponse(
                this.piloteService.update(this.pilote));
        } else {
            this.subscribeToSaveResponse(
                this.piloteService.create(this.pilote));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Pilote>>) {
        result.subscribe((res: HttpResponse<Pilote>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Pilote) {
        this.eventManager.broadcast({ name: 'piloteListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackQualificationById(index: number, item: Qualification) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-pilote-popup',
    template: ''
})
export class PilotePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pilotePopupService: PilotePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pilotePopupService
                    .open(PiloteDialogComponent as Component, params['id']);
            } else {
                this.pilotePopupService
                    .open(PiloteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
