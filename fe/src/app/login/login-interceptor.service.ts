import { Injectable } from '@angular/core';
import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpHeaders
} from '@angular/common/http';
import { take, exhaustMap } from 'rxjs/operators';

import { LoginService } from './login.service';

@Injectable({ providedIn: 'root' })
export class LoginInterceptorService implements HttpInterceptor {
    constructor(private loginService: LoginService) { }
    intercept(req: HttpRequest<any>, next: HttpHandler) {
        return this.loginService.user.pipe(
            take(1),
            exhaustMap(user => {
                if (!user) {
                    return next.handle(req);
                }
                const modifiedReq = req.clone({
                    headers: new HttpHeaders().set('Authorization', `Bearer ${user.token}`)
                });
                return next.handle(modifiedReq);
            })
        );
    }
}
