import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Reservoir } from './reservoir.model';
import { ReservoirPopupService } from './reservoir-popup.service';
import { ReservoirService } from './reservoir.service';

@Component({
    selector: 'jhi-reservoir-delete-dialog',
    templateUrl: './reservoir-delete-dialog.component.html'
})
export class ReservoirDeleteDialogComponent {

    reservoir: Reservoir;

    constructor(
        private reservoirService: ReservoirService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reservoirService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'reservoirListModification',
                content: 'Deleted an reservoir'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reservoir-delete-popup',
    template: ''
})
export class ReservoirDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reservoirPopupService: ReservoirPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.reservoirPopupService
                .open(ReservoirDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
