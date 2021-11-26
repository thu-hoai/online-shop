import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { PickListModule } from 'primeng/picklist';

import { UsersComponent } from './list/users.component';
import { UsersRoutingModule } from './users-routing.module';
import { AddUsersComponent } from './add/add-users.component';

import { CommonService } from '../../services/common.service';
import { UsersService } from '../../services/users.service';
import { SweetAlertService } from 'angular-sweetalert-service/js';
import { ApplicationsService } from '../../services/apps.service';
import { DataService } from '../../services/data.service';

@NgModule({
  imports: [
    CommonModule,
    // HttpClientModule,
    ReactiveFormsModule,
    RouterModule,
    UsersRoutingModule,
    TableModule,
    TooltipModule,
    PickListModule
  ],
  providers: [
    DataService,
    CommonService,
    UsersService,
    SweetAlertService,
    ApplicationsService
  ],
  declarations: [UsersComponent, AddUsersComponent]
})
export class UsersModule { }
