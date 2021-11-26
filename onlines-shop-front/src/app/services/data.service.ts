import { Injectable } from "@angular/core";

@Injectable()
export class DataService {

    private data: any = null;

    setData(data: any): any {
        this.data = data;
    }

    getData(): any {
        let data: any = this.data;
        this.data = null;

        return data;
    }
}