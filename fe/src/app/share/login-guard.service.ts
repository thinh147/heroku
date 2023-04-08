import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { map, Observable, take } from 'rxjs';
import { LoginService } from '../login/login.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuardService implements CanActivate {
  constructor(private loginService: LoginService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot):
    | Promise<boolean | UrlTree>
    | Observable<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.loginService.user.pipe(
      take(1),//sau khi lấy dữ liệu user mới nhất thì tự đồng unsubscribe
      map(user => {
        const isAuth = !!user;
        if (isAuth) {
          return true;//nếu đã login thì cho đi tiếp
        }
        return this.router.createUrlTree(['/login']);//nếu chưa login thì trả về UrlTree để chuyển hướng trang
      }));
  }
}
