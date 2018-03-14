import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PiloteComponent } from './pilote.component';
import { PiloteDetailComponent } from './pilote-detail.component';
import { PilotePopupComponent } from './pilote-dialog.component';
import { PiloteDeletePopupComponent } from './pilote-delete-dialog.component';

export const piloteRoute: Routes = [
    {
        path: 'pilote',
        component: PiloteComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.pilote.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pilote/:id',
        component: PiloteDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.pilote.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pilotePopupRoute: Routes = [
    {
        path: 'pilote-new',
        component: PilotePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.pilote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pilote/:id/edit',
        component: PilotePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.pilote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pilote/:id/delete',
        component: PiloteDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.pilote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
