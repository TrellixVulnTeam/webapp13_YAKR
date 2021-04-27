import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, map } from 'rxjs/operators'
import { Observable, throwError } from "rxjs";
import { User } from "../models/user.model";

@Injectable({ providedIn: 'root' })
export class LoginService {

    logged: boolean;
    user: User;

    constructor(private httpClient: HttpClient) {
        this.reqIsLogged();
    }

    reqIsLogged() {

        this.httpClient.get('/api/users/me', { withCredentials: true }).subscribe(
            response => {
                this.user = response as User;
                this.logged = true;
            },
            error => {
                this.logged = false;
                this.user = undefined;
            }
        );

    }

    logIn(user: string, pass: string) {

        this.httpClient.post("/api/auth/login", { username: user, password: pass }, { withCredentials: true })
            .subscribe(
                (response) => this.reqIsLogged(),
                (error) => alert("Wrong credentials")
            );

    }

    logOut() {

        /*return this.httpClient.post("/api/auth/logout", { withCredentials: true })
            .subscribe((resp: any) => {
                console.log("LOGOUT: Successfully");
                this.logged = false;
                this.user = undefined;
            },
            error => {
                console.log("LOGOUT: failed");
            }
        );*/

        
       return this.httpClient.post("/api/auth/logout", { withCredentials: true }).pipe(
            map(response => this.logOutConfirmed()),
            catchError(error => throwError('Server error'))
        );
        

    }

    logOutConfirmed(){
        this.logged = false;
        this.user = undefined;
        console.log("LOGOUT: Successfully")
    }

    isLogged() {
        return this.logged;
    }

    isAdmin() {
        return this.httpClient.get('/api/users/me/admin', { withCredentials: true }).subscribe(
            response => {
                response
            }
        );
    }

    currentUser() {
        return this.user;
    }

}
