import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DefaultLayoutComponent } from './containers/default-layout';

// Import Containers

import { CanActivateAuthGuard } from './services/security/can-activate.authguard';
import { LoginComponent } from './views/login/login.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: 'Login Page'
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
        path: 'products',
        canActivate: [CanActivateAuthGuard],
        loadChildren: () => import('./views/product/product.module').then(m => m.ProductModule)
      },
      {
        path: 'orders',
        canActivate: [CanActivateAuthGuard],
        loadChildren: () => import('./views/order/order.module').then(m => m.OrderModule)
      },
      {
        path: 'users',
        canActivate: [CanActivateAuthGuard],
        loadChildren: () => import('./views/user/user.module').then(m => m.UserModule)
      }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
