import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from '../../environments/environment';
import { Observable } from "rxjs";
import { VerificationToken } from "../model/token.model";

@Injectable()
export class TokenService {

    readonly endpoint: string = '/auth';

    constructor(private http: HttpClient) {
    }

    verifyToken(token: string): Observable<VerificationToken> {
        return this.http.get<VerificationToken>(`${environment.apiUrl}${this.endpoint}/verify-token/${token}`);
    }

    resendActivationLink(token: string): any {
        return this.http.post<VerificationToken>(`${environment.apiUrl}${this.endpoint}/resend-activation-link`, token);
    }

    setPassword(token: string, password: string): any {
        return this.http.post<VerificationToken>(`${environment.apiUrl}${this.endpoint}/set-password`, {
            token: token,
            password: password
        });
    }
}