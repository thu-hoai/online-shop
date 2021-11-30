import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderRoutingModule } from './order-routing.module';
import { OrderListComponent } from './order-list/order-list.component';
import { OrderService } from 'src/app/services/order.service';
import { TableModule } from 'primeng';



@NgModule({
  declarations: [OrderListComponent],
  imports: [
    CommonModule,
    OrderRoutingModule,
    TableModule,
  ],
  providers: [
    OrderService
  ],
})
export class OrderModule { }
