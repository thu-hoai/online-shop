import { NgModule } from '@angular/core';
import {
  Routes,
  RouterModule
} from '@angular/router';
import { ProductAddComponent } from './product-add/product-add.component';
import { ProductItemComponent } from './product-item/product-item.component';
import { ProductListComponent } from './product-list/product-list.component';


const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Products'
    },
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'list'
      },
      {
        path: 'list',
        component: ProductListComponent,
        data: {
          title: 'List'
        }
      },
      {
        path: ':id',
        component: ProductItemComponent,
        data: {
          title: 'details'
        }
      },
      {
        path: 'add',
        component: ProductAddComponent,
        data: {
          title: 'Add'
        }
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductRoutingModule { }
