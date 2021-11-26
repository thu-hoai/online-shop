import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {
  AppAsideModule,
  AppBreadcrumbModule,
  AppHeaderModule,
  AppFooterModule,
  AppSidebarModule,
} from '@coreui/angular'
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { AppComponent } from './app.component';
import { AuthenticationService } from './services/auth.service';
// Import 3rd party components
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { TokenStorage } from './services/token.storage';
import { AuthInterceptor } from './services/security/auth.interceptor';
import { CanActivateAuthGuard } from './services/security/can-activate.authguard';

import { ReactiveFormsModule } from '@angular/forms';
import { DefaultLayoutComponent } from './containers/default-layout';
import { LoginComponent } from './views/login/login.component';
import { AppRoutingModule } from './app.routing';
import { SweetAlertService } from 'angular-sweetalert-service/js';


const APP_CONTAINERS = [
  DefaultLayoutComponent
];

@NgModule({
  declarations: [
    AppComponent,
    ...APP_CONTAINERS,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    AppAsideModule,
    AppBreadcrumbModule.forRoot(),
    AppFooterModule,
    AppHeaderModule,
    AppSidebarModule,
    PerfectScrollbarModule,
    BsDropdownModule.forRoot(),
    TabsModule.forRoot(),
    HttpClientModule,
    ReactiveFormsModule,

  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  },
  CanActivateAuthGuard,
  SweetAlertService,
  AuthenticationService,
  TokenStorage],
  bootstrap: [AppComponent]
})
export class AppModule { }
