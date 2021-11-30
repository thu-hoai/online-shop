import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { TokenStorage } from '../token.storage';
import { tap } from 'rxjs/operators';
import 'rxjs/operators';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private token: TokenStorage, private router: Router) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        let authReq = req;

        if (this.token.getToken() != null) {
            authReq = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + this.token.getToken()) });
        }

        return next.handle(authReq).pipe(tap(
            (err: any) => {
                if (err instanceof HttpErrorResponse) {

                    if (err.status === 401) {
                        this.router.navigate(['/login']);
                    }
                }
            }
        ));
  }
}
