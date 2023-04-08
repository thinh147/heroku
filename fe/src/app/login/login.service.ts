import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { User } from './login.component';

// import { stringify } from 'querystring';

export interface LoginResponseData {
    details:{
        accessToken: string;
    }
    // accessToken: string;
    // expiryDuration: number;
    // refreshToken: string;
    // tokenType: number;
}



@Injectable({
    providedIn: 'root'
})
export class LoginService {

    user = new BehaviorSubject<User>(null);
    constructor(
        private http: HttpClient
        , private router: Router
    ) { }

    login(username: string, password: string) {
        const url = new URL(environment.baseURL + "/auth/login");
        const data = {
            phoneNumber: '',
            password: '',
        }
        data.phoneNumber = username;
        data.password = password;
        const httpOptions = {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        }
        return this.http.post<LoginResponseData>(url.href, data, httpOptions);
    }

    logout() {
        this.user.next(null);
        this.router.navigate(['/login']);
        sessionStorage.removeItem('userData');
    }

    autoLogin() {
        const userData: {
            username: string;
            token: string;
        } = JSON.parse(sessionStorage.getItem('userData'));
        if (!userData) {
            return;
        }
        if (userData.token) {
            this.user.next(userData);
        }
    }

}