import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Carburant } from './carburant.model';
import { CarburantPopupService } from './carburant-popup.service';
import { CarburantService } from './carburant.service';

@Component({
    selector: 'jhi-carburant-delete-dialog',
    templateUrl: './carburant-delete-dialog.component.html'
})
export class CarburantDeleteDialogComponent {

    carburant: Carburant;

    constructor(
        private carburantService: CarburantService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.carburantService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'carburantListModification',
                content: 'Deleted an carburant'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-carburant-delete-popup',
    template: ''
})
export class CarburantDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private carburantPopupService: CarburantPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.carburantPopupService
                .open(CarburantDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
