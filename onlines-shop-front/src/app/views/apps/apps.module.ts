import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { PickListModule } from 'primeng/picklist';
import { CommonModule } from '@angular/common';

import { ApplicationsComponent } from './list/apps.component';
import { ApplicationsRoutingModule } from './apps-routing.module';
import { AddApplicationsComponent } from './add/add-apps.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { DataService } from '../../services/data.service';
import { SweetAlertService } from 'angular-sweetalert-service/js';
import { ApplicationsService } from '../../services/apps.service';

@NgModule({
  imports: [
    CommonModule,
    // HttpClientModule,
    ReactiveFormsModule,
    RouterModule,
    ApplicationsRoutingModule,
    TableModule,
    TooltipModule,
    PickListModule
  ],
  providers: [
    DataService,
    SweetAlertService,
    ApplicationsService
  ],
  declarations: [ApplicationsComponent, AddApplicationsComponent]
})
export class ApplicationsModule { }
