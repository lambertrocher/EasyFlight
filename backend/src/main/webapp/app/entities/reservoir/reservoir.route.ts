import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ReservoirComponent } from './reservoir.component';
import { ReservoirDetailComponent } from './reservoir-detail.component';
import { ReservoirPopupComponent } from './reservoir-dialog.component';
import { ReservoirDeletePopupComponent } from './reservoir-delete-dialog.component';

export const reservoirRoute: Routes = [
    {
        path: 'reservoir',
        component: ReservoirComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.reservoir.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'reservoir/:id',
        component: ReservoirDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.reservoir.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reservoirPopupRoute: Routes = [
    {
        path: 'reservoir-new',
        component: ReservoirPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.reservoir.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reservoir/:id/edit',
        component: ReservoirPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.reservoir.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reservoir/:id/delete',
        component: ReservoirDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.reservoir.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
