import { Application } from "./app.model";

export interface User {
    id: number;
    firstName: string;
    lastName: string;
    active: boolean;
    email: string;
    department: string;
    applications?: Application[];
}