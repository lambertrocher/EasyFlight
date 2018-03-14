import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AerodromeComponent } from './aerodrome.component';
import { AerodromeDetailComponent } from './aerodrome-detail.component';
import { AerodromePopupComponent } from './aerodrome-dialog.component';
import { AerodromeDeletePopupComponent } from './aerodrome-delete-dialog.component';

export const aerodromeRoute: Routes = [
    {
        path: 'aerodrome',
        component: AerodromeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.aerodrome.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'aerodrome/:id',
        component: AerodromeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.aerodrome.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const aerodromePopupRoute: Routes = [
    {
        path: 'aerodrome-new',
        component: AerodromePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.aerodrome.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'aerodrome/:id/edit',
        component: AerodromePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.aerodrome.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'aerodrome/:id/delete',
        component: AerodromeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backendApp.aerodrome.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
