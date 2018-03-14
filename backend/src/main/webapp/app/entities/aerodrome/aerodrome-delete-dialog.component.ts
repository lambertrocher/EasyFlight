import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Aerodrome } from './aerodrome.model';
import { AerodromePopupService } from './aerodrome-popup.service';
import { AerodromeService } from './aerodrome.service';

@Component({
    selector: 'jhi-aerodrome-delete-dialog',
    templateUrl: './aerodrome-delete-dialog.component.html'
})
export class AerodromeDeleteDialogComponent {

    aerodrome: Aerodrome;

    constructor(
        private aerodromeService: AerodromeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.aerodromeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'aerodromeListModification',
                content: 'Deleted an aerodrome'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-aerodrome-delete-popup',
    template: ''
})
export class AerodromeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private aerodromePopupService: AerodromePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.aerodromePopupService
                .open(AerodromeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
