import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CarburantComponent } from './carburant.component';
import { CarburantDetailComponent } from './carburant-detail.component';
import { CarburantPopupComponent } from './carburant-dialog.component';
import { CarburantDeletePopupComponent } from './carburant-delete-dialog.component';

export const carburantRoute: Routes = [
    {
        path: 'carburant',
        component: CarburantComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.carburant.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'carburant/:id',
        component: CarburantDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.carburant.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const carburantPopupRoute: Routes = [
    {
        path: 'carburant-new',
        component: CarburantPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.carburant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'carburant/:id/edit',
        component: CarburantPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.carburant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'carburant/:id/delete',
        component: CarburantDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.carburant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
