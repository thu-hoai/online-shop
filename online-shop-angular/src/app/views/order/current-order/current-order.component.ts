import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, switchMap } from 'rxjs/operators';
import { OrderService } from 'src/app/services/order.service';
import { TokenStorage } from 'src/app/services/token.storage';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-current-order',
  templateUrl: './current-order.component.html',
  styleUrls: ['./current-order.component.css'],
})
export class CurrentOrderComponent implements OnInit, OnDestroy {
  constructor(
    private _orderService: OrderService,
    private _userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private _tokenStorage: TokenStorage
  ) {}

  productInOrders:any[] = [];
  total = 0;
  currentUser: User;
  userSubscription: Subscription;

  private updateTerms = new Subject<Product>();
  sub: Subscription;

  static validateCount(productInOrder:Product) {
    // const max = productInOrder.productStock;
    // if (productInOrder.count > max) {
    //   productInOrder.count = max;
    // } else if (productInOrder.count < 1) {
    //   productInOrder.count = 1;
    // }
    // console.log(productInOrder.count);
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.currentUser = this._tokenStorage.getCurrentUser();
    if (id !== null) {
      this._orderService.getOrderByOrderId(id).subscribe((prods) => {
        this.productInOrders = prods;
      });
    }

    this.sub = this.updateTerms
      .pipe(
        // wait 300ms after each keystroke before considering the term
        debounceTime(300),
        //
        // ignore new term if same as previous term
        // Same Object Reference, not working here
        //  distinctUntilChanged((p: ProductInOrder, q: ProductInOrder) => p.count === q.count),
        //
        // switch to new search observable each time the term changes
        // switchMap((productInOrder: Product) =>
        //   this._orderService.update(productInOrder)
        // )
      )
      .subscribe(
        (prod) => {
          if (prod) {
            throw new Error();
          }
        },
        (_) => console.log('Update Item Failed')
      );
  }

  ngOnDestroy() {
    if (!this.currentUser) {
      // this._orderService.storeLocalCart();
    }
    this.userSubscription.unsubscribe();
  }

  ngAfterContentChecked() {
    this.total = this.productInOrders.reduce(
      (prev, cur) => prev + cur.count * cur.productPrice,
      0
    );
  }

  addOne(productInOrder:Product) {
    // productInOrder.count++;
    // CartComponent.validateCount(productInOrder);
    // if (this.currentUser) {
    //   this.updateTerms.next(productInOrder);
    // }
  }

  minusOne(productInOrder:Product) {
    // productInOrder.count--;
    // CartComponent.validateCount(productInOrder);
    // if (this.currentUser) {
    //   this.updateTerms.next(productInOrder);
    // }
  }

  onChange(productInOrder:Product) {
    // CartComponent.validateCount(productInOrder);
    // if (this.currentUser) {
    //   this.updateTerms.next(productInOrder);
    // }
  }

  remove(productInOrder: Product) {
    // this._orderService.remove(productInOrder).subscribe(
    //   (success) => {
    //     this.productInOrders = this.productInOrders.filter(
    //       (e) => e.productId !== productInOrder.productId
    //     );
    //     console.log('Cart: ' + this.productInOrders);
    //   },
    //   (_) => console.log('Remove Cart Failed')
    // );
  }

  checkout() {
    // if (!this.currentUser) {
    //   this.router.navigate(['/login'], {
    //     queryParams: { returnUrl: this.router.url },
    //   });
    // } else if (this.currentUser.role !== Role.Customer) {
    //   this.router.navigate(['/seller']);
    // } else {
    //   this._orderService.checkout().subscribe(
    //     (_) => {
    //       this.productInOrders = [];
    //     },
    //     (error1) => {
    //       console.log('Checkout Cart Failed');
    //     }
    //   );
    //   this.router.navigate(['/']);
    // }
  }
}
