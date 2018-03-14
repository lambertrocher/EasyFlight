import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AvionComponent } from './avion.component';
import { AvionDetailComponent } from './avion-detail.component';
import { AvionPopupComponent } from './avion-dialog.component';
import { AvionDeletePopupComponent } from './avion-delete-dialog.component';

export const avionRoute: Routes = [
    {
        path: 'avion',
        component: AvionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.avion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'avion/:id',
        component: AvionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.avion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const avionPopupRoute: Routes = [
    {
        path: 'avion-new',
        component: AvionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.avion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'avion/:id/edit',
        component: AvionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.avion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'avion/:id/delete',
        component: AvionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.avion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
