import { NgModule } from '@angular/core';
import {
  Routes,
  RouterModule
} from '@angular/router';

import { ApplicationsComponent } from './list/apps.component';
import { AddApplicationsComponent } from './add/add-apps.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Applications'
    },
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'list'
      },
      {
        path: 'list',
        component: ApplicationsComponent,
        data: {
          title: 'Liste'
        }
      },
      {
        path: 'add',
        component: AddApplicationsComponent,
        data: {
          title: 'Ajout'
        }
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplicationsRoutingModule { }
