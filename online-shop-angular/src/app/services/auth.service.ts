import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenStorage } from './token.storage';
import { environment } from '../../environments/environment';


@Injectable()
export class AuthenticationService {
  readonly endpoint: string = '/auth';

  constructor(private http: HttpClient, private _storage: TokenStorage) {
  }

  login(username: string, password: string): Observable<any> {
      return this.http.post(`${environment.apiUrl}${this.endpoint}/login`, {
          username: username,
          password: password
      });
  }

  saveLoginData(data: JwtUser) {
      this._storage.save(data.token, data.user);
  }

  getCurrentUser() {
      return this._storage.getCurrentUser();
  }

  isLoggedIn(): boolean {
      let token: string = this._storage.getToken();
      return token != null && token.length > 0;
  }

  logout(): void {
      this._storage.clear();
  }
}
