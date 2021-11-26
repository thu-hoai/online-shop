import { NgModule } from '@angular/core';
import {
  Routes,
  RouterModule
} from '@angular/router';
import { CurrentOrderComponent } from './current-order/current-order.component';
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
      {
        path: ':id',
        component: CurrentOrderComponent,
        data: {
          title: 'details'
        }
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderRoutingModule { }
