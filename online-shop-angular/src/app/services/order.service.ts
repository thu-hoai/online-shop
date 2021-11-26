import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  readonly endpoint: string = '/orders';


  constructor(private http: HttpClient) {
  }

  createNewOrder() : Observable<any>{
    return this.http.post(`${environment.apiUrl}${this.endpoint}`, null);
  }

  addItemToOrder(item: OrderItemRequest, orderId: string) : Observable<any>{
    return this.http.post(`${environment.apiUrl}${this.endpoint}/${orderId}`, item);
  }

  getOrderByOrderId(orderId: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}${this.endpoint}/${orderId}`);
  }
}
