import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CurrentOrderComponent } from './current-order/current-order.component';
import { OrderRoutingModule } from './order-routing.module';
import { OrderListComponent } from './order-list/order-list.component';
import { OrderService } from 'src/app/services/order.service';



@NgModule({
  declarations: [CurrentOrderComponent, OrderListComponent],
  imports: [
    CommonModule,
    OrderRoutingModule
  ],
  providers: [
    OrderService
  ],
})
export class OrderModule { }
