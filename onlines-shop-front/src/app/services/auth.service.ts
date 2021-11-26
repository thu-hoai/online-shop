import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from '../../environments/environment';
import { Observable } from "rxjs";
import { TokenStorage } from "./token.storage";

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

    loginSso(): Observable<any> {
        return this.http.get(`${environment.apiUrl}${this.endpoint}/sso`);
    }

    saveLoginData(data) {
        this._storage.save(data.token, data.currentUser);
    }

    getCurrentUser() {
        return this._storage.getCurrentUser();
    }

    isLoggedIn(): boolean {
        let token = this._storage.getToken();
        return token && token.length > 0;
    }

    logout(): void {
        this._storage.clear();
    }
}