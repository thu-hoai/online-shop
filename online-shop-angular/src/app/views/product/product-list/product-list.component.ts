import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { userInfo } from 'os';
import { ProductService } from 'src/app/services/product.service';
import { TokenStorage } from 'src/app/services/token.storage';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  constructor(private router: Router,
    private _productService: ProductService,
    private _tokenStorage: TokenStorage) { }

  products: Product[] = [];
  currentUser: User;
  isAdminUser: boolean;
  status: boolean = false;

  clickEvent(){
      this.status = !this.status;
  }
  ngOnInit(): void {
    console.log("Loadding products")
    this.loadPaginatedProducts();
    this.currentUser = this._tokenStorage.getCurrentUser();
    this.isAdminUser = this.checkAdminUser();
  }

  loadPaginatedProducts() {
    this._productService.getPaginatedByProduct().subscribe(data => this.products = data.content);
  }

  checkAdminUser(): boolean {
    for (let i = 0; i < this.currentUser.authorities.length; i++) {
      if (this.currentUser.authorities[i].name  === "Administrator") {
        return true;
      };
    }
    return false;
  }

}
