import { NgModule } from '@angular/core';
import {
  Routes,
  RouterModule
} from '@angular/router';

import { UsersComponent } from './list/users.component';
import { AddUsersComponent } from './add/add-users.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Utilisateurs'
    },
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'list'
      },
      {
        path: 'list',
        component: UsersComponent,
        data: {
          title: 'Liste'
        }
      },
      {
        path: 'add',
        component: AddUsersComponent,
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
export class UsersRoutingModule { }
