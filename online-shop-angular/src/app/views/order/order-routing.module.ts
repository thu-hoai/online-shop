import { NgModule } from '@angular/core';
import {
  Routes,
  RouterModule
} from '@angular/router';
import { OrderListComponent } from './order-list/order-list.component';


const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Orders'
    },
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'list'
      },
      {
        path: 'list',
        component: OrderListComponent,
        data: {
          title: 'List'
        }
      },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderRoutingModule { }
