import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Vol } from './vol.model';
import { VolPopupService } from './vol-popup.service';
import { VolService } from './vol.service';

@Component({
    selector: 'jhi-vol-delete-dialog',
    templateUrl: './vol-delete-dialog.component.html'
})
export class VolDeleteDialogComponent {

    vol: Vol;

    constructor(
        private volService: VolService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.volService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'volListModification',
                content: 'Deleted an vol'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vol-delete-popup',
    template: ''
})
export class VolDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private volPopupService: VolPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.volPopupService
                .open(VolDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
