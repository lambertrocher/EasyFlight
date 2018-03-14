import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Pilote } from './pilote.model';
import { PiloteService } from './pilote.service';

@Injectable()
export class PilotePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private piloteService: PiloteService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.piloteService.find(id)
                    .subscribe((piloteResponse: HttpResponse<Pilote>) => {
                        const pilote: Pilote = piloteResponse.body;
                        if (pilote.dateValiditeLicence) {
                            pilote.dateValiditeLicence = {
                                year: pilote.dateValiditeLicence.getFullYear(),
                                month: pilote.dateValiditeLicence.getMonth() + 1,
                                day: pilote.dateValiditeLicence.getDate()
                            };
                        }
                        this.ngbModalRef = this.piloteModalRef(component, pilote);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.piloteModalRef(component, new Pilote());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    piloteModalRef(component: Component, pilote: Pilote): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.pilote = pilote;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
