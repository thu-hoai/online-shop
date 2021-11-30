import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { MessageService } from 'src/app/services/message.service';
import { OrderService } from 'src/app/services/order.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-item',
  templateUrl: './product-item.component.html',
  styleUrls: ['./product-item.component.css'],
})
export class ProductItemComponent implements OnInit {
  title: string;
  count: number;
  productInfo: Product;
  currentOrder: Order;
  currentOrderId: string;
  currentOrderIdSubscription: Subscription;

  constructor(
    private _productService: ProductService,
    private _orderService: OrderService,
    private route: ActivatedRoute,
    private router: Router,
    private _dataService: MessageService
  ) {
    this.currentOrderIdSubscription = this._dataService
      .getCurrentOrderId()
      .subscribe((data) => {
        console.log(data);
        this.currentOrderId = data.text;
        const request: OrderItemRequest = {
          productId: this.productInfo.productId,
          quantity: this.count,
        };
        this._orderService
          .addItemToOrder(request, this.currentOrderId)
          .subscribe((res) => {
            this.router.navigateByUrl('/cart');
            // this.router.navigateByUrl('/orders/' + this.currentOrderId);
          });
      });
  }

  ngOnInit() {
    this.getProduct();
    this.title = 'Product Detail';
    this.count = 1;
  }

  getProduct(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id !== null) {
      this._productService.getProductById(id).subscribe(
        (prod) => {
          this.productInfo = prod;
        },
        () => console.log('Get Cart Failed')
      );
    }
  }

  addToCart() {
    console.log('test');
    if (this.currentOrderId === undefined) {
      this._orderService.createNewOrder().subscribe(
        (createdOrder) => {
          this.currentOrder = createdOrder;
          this._dataService.sendCurrentOrderId(createdOrder.orderId);
        },
        () => console.log('Create Cart Failed')
      );
    } else {
      this._orderService.getOrderByOrderId(this.currentOrderId).subscribe(
        (order) => {
          this.currentOrder = order;
          this.router.navigate(['/cart']);

          // this.router.navigate(['/orders/' + this.currentOrderId]);
          // this.router.navigateByUrl('/cart/' + this.currentOrderId);
        },
        () => console.log('Get order by order id failed')
      );
    }
  }

  validateCount() {
    const max = this.productInfo.productStock;
    if (this.count > max) {
      this.count = max;
    } else if (this.count < 1) {
      this.count = 1;
    }
  }
}
