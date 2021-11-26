import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { PickListModule } from 'primeng/picklist';
import { ProductListComponent } from './product-list/product-list.component';
import { ProductAddComponent } from './product-add/product-add.component';
import { ProductRoutingModule } from './product-routing.module';
import { ProductService } from 'src/app/services/product.service';
import { CommonModule } from '@angular/common';
import { ProductItemComponent } from './product-item/product-item.component';

@NgModule({
  imports: [
    FormsModule,
    ProductRoutingModule,
    TableModule,
    PickListModule,
    TooltipModule,
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [
    ProductService
  ],
  declarations: [ ProductListComponent, ProductAddComponent, ProductItemComponent ]
})
export class ProductModule { }
