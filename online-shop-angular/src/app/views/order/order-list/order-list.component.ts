import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {

  constructor(private router: Router,
    private _orderService: OrderService,
    ) { }

  orders: Order[] = [];
  status: boolean = false;

  clickEvent(){
      this.status = !this.status;
  }
  ngOnInit(): void {
    this.loadPaginatedOrders();
  }

  loadPaginatedOrders() {
    this._orderService.getOrders().subscribe(data => this.orders = data.content);
  }

}
