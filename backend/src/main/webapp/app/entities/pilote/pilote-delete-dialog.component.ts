import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pilote } from './pilote.model';
import { PilotePopupService } from './pilote-popup.service';
import { PiloteService } from './pilote.service';

@Component({
    selector: 'jhi-pilote-delete-dialog',
    templateUrl: './pilote-delete-dialog.component.html'
})
export class PiloteDeleteDialogComponent {

    pilote: Pilote;

    constructor(
        private piloteService: PiloteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.piloteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'piloteListModification',
                content: 'Deleted an pilote'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pilote-delete-popup',
    template: ''
})
export class PiloteDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pilotePopupService: PilotePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pilotePopupService
                .open(PiloteDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
