import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private subject = new Subject<any>();
  constructor() { }

  sendCurrentOrderId(message: string) {
    this.subject.next({ text: message });
  }

  getCurrentOrderId(): Observable<any> {
    return this.subject.asObservable();
  }
}
