import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Vol } from './vol.model';
import { VolService } from './vol.service';

@Injectable()
export class VolPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private volService: VolService

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
                this.volService.find(id)
                    .subscribe((volResponse: HttpResponse<Vol>) => {
                        const vol: Vol = volResponse.body;
                        if (vol.dateVol) {
                            vol.dateVol = {
                                year: vol.dateVol.getFullYear(),
                                month: vol.dateVol.getMonth() + 1,
                                day: vol.dateVol.getDate()
                            };
                        }
                        this.ngbModalRef = this.volModalRef(component, vol);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.volModalRef(component, new Vol());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    volModalRef(component: Component, vol: Vol): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.vol = vol;
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
