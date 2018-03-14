import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Piste } from './piste.model';
import { PistePopupService } from './piste-popup.service';
import { PisteService } from './piste.service';

@Component({
    selector: 'jhi-piste-delete-dialog',
    templateUrl: './piste-delete-dialog.component.html'
})
export class PisteDeleteDialogComponent {

    piste: Piste;

    constructor(
        private pisteService: PisteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pisteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pisteListModification',
                content: 'Deleted an piste'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-piste-delete-popup',
    template: ''
})
export class PisteDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pistePopupService: PistePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pistePopupService
                .open(PisteDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
