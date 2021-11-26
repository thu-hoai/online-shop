import { Injectable } from "@angular/core";
import { LazyLoadEvent } from "primeng/primeng";

Injectable()
export class CommonService {

    private buildCriterias(filters?: any): string {

        let filter: string[] = [];

        for (let attr in filters) {
            console.log(attr);
            filter.push(attr + ':*' + filters[attr].value + '*');
        }

        return filter.join(',');
    }

    buildPaginationParams(event: LazyLoadEvent): string {
        let filter: string = this.buildCriterias(event.filters);
        let direction = event.sortOrder == 1 ? 'asc' : 'desc';

        if (filter && filter != '') {
            filter = '&search=' + filter;
        }

        return `?page=${event.first / event.rows}&size=${event.rows}&sort=${event.sortField},${direction}${filter}`;
    }
}