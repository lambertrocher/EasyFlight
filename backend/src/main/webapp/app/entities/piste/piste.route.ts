import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PisteComponent } from './piste.component';
import { PisteDetailComponent } from './piste-detail.component';
import { PistePopupComponent } from './piste-dialog.component';
import { PisteDeletePopupComponent } from './piste-delete-dialog.component';

export const pisteRoute: Routes = [
    {
        path: 'piste',
        component: PisteComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.piste.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'piste/:id',
        component: PisteDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.piste.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pistePopupRoute: Routes = [
    {
        path: 'piste-new',
        component: PistePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.piste.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'piste/:id/edit',
        component: PistePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.piste.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'piste/:id/delete',
        component: PisteDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.piste.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
