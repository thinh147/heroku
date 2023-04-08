import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  formLogin = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  })
  constructor(
    private loginService: LoginService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    const user = this.loginService.user.getValue();
    if (user) {
      console.log("a")
      this.router.navigate(['/question']);
    }
  }

  checkValidators(control: AbstractControl) {
    return (control.invalid && control.touched) || (control.invalid && control.dirty)
  }

  onSubmitLogin() {
    if (this.formLogin.get('username').invalid) {
      this.formLogin.get('username').markAsTouched();
      return;
    }
    if (this.formLogin.get('password').invalid) {
      this.formLogin.get('password').markAsTouched();
      return;
    }
    this.loginService.login(this.formLogin.get('username').value, this.formLogin.get('password').value).subscribe({
      next: data => {
        const user = new User();
        user.username = this.formLogin.get('username').value;
        user.token = data.details.accessToken;
        this.loginService.user.next(user);
        sessionStorage.setItem('userData', JSON.stringify(user));
        this.formLogin.reset();
        this.router.navigate(['/question']);
      }, error: err => {
        alert('Sai thông tin đăng nhập')
        console.log(err);//login service có pipe error để chỉ output errorMessage dạng string
      }
    })
  }

}
export class User {
  public username: string//thông tin có thể lấy từ token
  public token: string;
}