import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SweetAlertService } from 'angular-sweetalert-service';
import { Subject } from 'rxjs';
import { OrderService } from 'src/app/services/order.service';
import { TokenStorage } from 'src/app/services/token.storage';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit, OnDestroy {
  constructor(
    private _orderService: OrderService,
    private router: Router,
    private route: ActivatedRoute,
    private _alert: SweetAlertService,
    private _tokenStorage: TokenStorage
  ) {}

  cart: Order;
  productInOrders: any[] = [];
  total = 0;
  currentUser: User;

  private updateTerms = new Subject<OrderItem>();

  static validateCount(productInOrder: OrderItem) {
    const max = productInOrder.productStock;
    if (productInOrder.quantity > max) {
      productInOrder.quantity = max;
    } else if (productInOrder.quantity < 1) {
      productInOrder.quantity = 1;
    }
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.currentUser = this._tokenStorage.getCurrentUser();
    if (id !== null) {
      this._orderService.getOrderByOrderId(id).subscribe((prods) => {
        this.cart = prods;
        this.productInOrders = prods.orderItem;
      });
    } else {
      this._orderService.getCurrentCart().subscribe((order) => {
        this.cart = order;
        this.productInOrders = order.orderItem;
      });
    }
  }

  ngOnDestroy() {}

  ngAfterContentChecked() {
    this.total = this.productInOrders.reduce(
      (prev, cur) => prev + cur.quantity * cur.price,
      0
    );
  }

  addOne(productInOrder: OrderItem) {
    productInOrder.quantity++;
    CartComponent.validateCount(productInOrder);
    if (this.currentUser) {
      this.updateTerms.next(productInOrder);
    }
  }

  minusOne(productInOrder: OrderItem) {
    productInOrder.quantity--;
    CartComponent.validateCount(productInOrder);
    if (this.currentUser) {
      this.updateTerms.next(productInOrder);
    }
  }

  onChange(productInOrder: OrderItem) {
    CartComponent.validateCount(productInOrder);
    if (this.currentUser) {
      this.updateTerms.next(productInOrder);
    }
  }

  remove(productInOrder: OrderItem) {
    this._alert
      .confirm({
        showCloseButton: true,
        confirmButtonText: '<i class="fa fa-trash"></i> Yes',
        showCancelButton: false,
        confirmButtonColor: '#f86c6b',
        title: `Do you want to remove « ${productInOrder.productName}» ?`,
      })
      .then((res: { value: any }) => {
        if (res.value) {
          this._orderService
            .removeOrderItem(
              productInOrder.orderId.toString(),
              productInOrder.productId.toString()
            )
            .subscribe(() => {
              this._alert.info({
                title: 'Success !',
                text: `Removed ${productInOrder.productName}`,
                showConfirmButton: false,
                timer: 1500,
              });
              this.productInOrders = this.productInOrders.filter(
                (e) => e.productId !== productInOrder.productId
              );
            });
        }
      });
  }

  checkout() {
    this._alert
      .confirm({
        showCloseButton: true,
        confirmButtonText: 'Yes',
        showCancelButton: false,
        confirmButtonColor: '#f86c6b',
        title: `Do you want to place this order ?`,
      })
      .then((res: { value: any }) => {
        if (res.value) {
          this._orderService
            .checkoutOrder(this.cart.orderId.toString())
            .subscribe(() => {
              this._alert.info({
                title: 'Success !',
                text: `Place order successfully`,
                showConfirmButton: false,
                timer: 1500,
              });
              this.productInOrders = [];
              this.router.navigate(['/products']);
            });
        }
      });
  }
}
