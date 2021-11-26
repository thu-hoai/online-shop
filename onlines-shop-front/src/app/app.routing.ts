import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Import Containers
import { DefaultLayoutComponent } from './containers';

import { P404Component } from './views/error/404.component';
import { P500Component } from './views/error/500.component';
import { LoginComponent } from './views/login/login.component';
import { ActivateAccountComponent } from './views/activate-account/activate-account.component';
import { CanActivateAuthGuard } from './services/security/can-activate.authguard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: '404',
    component: P404Component,
    data: {
      title: 'Page 404'
    }
  },
  {
    path: '500',
    component: P500Component,
    data: {
      title: 'Page 500'
    }
  },
  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: 'Login Page'
    }
  },
  {
    path: 'activate/:token',
    component: ActivateAccountComponent,
    data: {
      title: 'Activation Page'
    }
  },
  {
    path: '',
    component: DefaultLayoutComponent,
    data: {
      title: 'Home'
    },
    children: [
      {
        path: 'dashboard',
        canActivate: [CanActivateAuthGuard],
        loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'users',
        canActivate: [CanActivateAuthGuard],
        loadChildren: () => import('./views/users/users.module').then(m => m.UsersModule)
      },
      {
        path: 'applications',
        canActivate: [CanActivateAuthGuard],
        loadChildren: () => import('./views/apps/apps.module').then(m => m.ApplicationsModule)
      }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
