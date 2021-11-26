import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CommonService } from "./common.service";
import { LazyLoadEvent } from "primeng/primeng";
import { User } from "../model/user.model";
import { environment } from '../../environments/environment';
import { Application } from "../model/app.model";

@Injectable()
export class UsersService {

    readonly endpoint: string = '/users';
    readonly updateStatus:string = this.endpoint + '/status';

    constructor(private http: HttpClient, private _commonService: CommonService) {
    }

    loadUsers(event: LazyLoadEvent): Observable<User[]> {
        let requestParams:string = this._commonService.buildPaginationParams(event);
        return this.http.get<User[]>(`${environment.apiUrl}${this.endpoint}${requestParams}`);
    }

    changeUserStatus(id: number, status: boolean) {
        return this.http.post(`${environment.apiUrl}${this.updateStatus}`, {
            id: id,
            active: status
        });
    }

    storeUser(user: any, applications: Application[]) {
        let appIds:any[] = applications.map((value) => { return value.id });

        user.appIds = appIds;
        return this.http.post(`${environment.apiUrl}${this.endpoint}`, user);
    }

    deleteUser(id: number) {
        return this.http.delete(`${environment.apiUrl}${this.endpoint}/${id}`);
    }
}