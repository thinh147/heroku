import { Component } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { User } from '../login/login.component';
import { LoginService } from '../login/login.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  user: User;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private loginService: LoginService,
  ) { }
  sidebarState: string;
  ngOnInit(): void {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.urlAfterRedirects.indexOf('question') > 0) this.sidebarState = 'question';
        if (event.urlAfterRedirects.indexOf('user') > 0) this.sidebarState = 'user';
        if (event.urlAfterRedirects.indexOf('payment') > 0) this.sidebarState = 'payment';
        if (event.urlAfterRedirects.indexOf('exam') > 0) this.sidebarState = 'exam';
        if (event.urlAfterRedirects.indexOf('vocabulary') > 0) this.sidebarState = 'vocabulary';
      });

    this.loginService.user.subscribe({
      next: user => {
        this.user = user;
      }, error: error => {

      }
    });
  }
  getUser() {
    return this.user?.username;
  }
  onLogout() {
    this.loginService.logout;
  }
}
