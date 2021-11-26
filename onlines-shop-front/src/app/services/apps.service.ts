import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Application } from "../model/app.model";
import { Observable } from "rxjs";
import { environment } from '../../environments/environment';

@Injectable()
export class ApplicationsService {

    readonly endpoint: string = '/applications';

    constructor(private http: HttpClient) {
    }

    loadApps(): Observable<Application[]> {
        return this.http.get<Application[]>(`${environment.apiUrl}${this.endpoint}`);
    }

    storeApp(app: Application) {
        return this.http.post(`${environment.apiUrl}${this.endpoint}`, app);
    }

    deleteApp(id: number) {
        return this.http.delete(`${environment.apiUrl}${this.endpoint}/${id}`);
    }
}