import { Injectable } from '@angular/core';


const TOKEN_KEY = 'auth-token';
const USER_KEY = 'current-user';

@Injectable()
export class TokenStorage {

    storage: any;

    constructor() {
        this.storage = window.sessionStorage;
    }

    clear() {
        this.storage.removeItem(TOKEN_KEY);
        this.storage.removeItem(USER_KEY);

        this.storage.clear();
    }

    public save(token: string, currentUser: any) {
        this.clear();

        this.storage.setItem(TOKEN_KEY, token);
        this.storage.setItem(USER_KEY, JSON.stringify(currentUser));
    }

    public getToken(): string {
        return this.storage.getItem(TOKEN_KEY);
    }

    public getCurrentUser(): string {
        return JSON.parse(this.storage.getItem(USER_KEY));
    }
}