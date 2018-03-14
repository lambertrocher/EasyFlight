import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { VolComponent } from './vol.component';
import { VolDetailComponent } from './vol-detail.component';
import { VolPopupComponent } from './vol-dialog.component';
import { VolDeletePopupComponent } from './vol-delete-dialog.component';

export const volRoute: Routes = [
    {
        path: 'vol',
        component: VolComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.vol.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vol/:id',
        component: VolDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.vol.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const volPopupRoute: Routes = [
    {
        path: 'vol-new',
        component: VolPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.vol.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vol/:id/edit',
        component: VolPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.vol.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vol/:id/delete',
        component: VolDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.vol.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
