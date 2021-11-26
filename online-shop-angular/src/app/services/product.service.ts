import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  readonly endpoint: string = '/products';


  constructor(private http: HttpClient) {
  }

  getPaginatedByProduct() : Observable<any>{
    return this.http.get(`${environment.apiUrl}${this.endpoint}`)
  }

  getProductById(productId: string) : Observable<any>{
    return this.http.get(`${environment.apiUrl}${this.endpoint}/${productId}`)
  }
}
