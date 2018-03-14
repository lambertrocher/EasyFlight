import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Avion } from './avion.model';
import { AvionPopupService } from './avion-popup.service';
import { AvionService } from './avion.service';

@Component({
    selector: 'jhi-avion-delete-dialog',
    templateUrl: './avion-delete-dialog.component.html'
})
export class AvionDeleteDialogComponent {

    avion: Avion;

    constructor(
        private avionService: AvionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.avionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'avionListModification',
                content: 'Deleted an avion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-avion-delete-popup',
    template: ''
})
export class AvionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private avionPopupService: AvionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.avionPopupService
                .open(AvionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
