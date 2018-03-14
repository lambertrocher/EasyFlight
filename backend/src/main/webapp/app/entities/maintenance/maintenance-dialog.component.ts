import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Maintenance } from './maintenance.model';
import { MaintenancePopupService } from './maintenance-popup.service';
import { MaintenanceService } from './maintenance.service';

@Component({
    selector: 'jhi-maintenance-dialog',
    templateUrl: './maintenance-dialog.component.html'
})
export class MaintenanceDialogComponent implements OnInit {

    maintenance: Maintenance;
    isSaving: boolean;
    dateMaintenanceDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private maintenanceService: MaintenanceService,
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
        if (this.maintenance.id !== undefined) {
            this.subscribeToSaveResponse(
                this.maintenanceService.update(this.maintenance));
        } else {
            this.subscribeToSaveResponse(
                this.maintenanceService.create(this.maintenance));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Maintenance>>) {
        result.subscribe((res: HttpResponse<Maintenance>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Maintenance) {
        this.eventManager.broadcast({ name: 'maintenanceListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-maintenance-popup',
    template: ''
})
export class MaintenancePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private maintenancePopupService: MaintenancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.maintenancePopupService
                    .open(MaintenanceDialogComponent as Component, params['id']);
            } else {
                this.maintenancePopupService
                    .open(MaintenanceDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
