import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SweetAlertService } from 'angular-sweetalert-service/js';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'login.component.html',
})
export class LoginComponent {
  body: any;
  loginForm!: FormGroup;

  public constructor(
    private router: Router,
    private fb: FormBuilder,
    private _authService: AuthenticationService,
    private _alert: SweetAlertService
  ) {
    this.body = document.getElementsByTagName('body')[0];
    this._authService.logout();
  }

  onFormSubmit() {
    if (this.loginForm.valid) {
      let value = this.loginForm.value;

      this._authService.login(value.username, value.password).subscribe(
        (data: JwtUser) => {
          this._authService.saveLoginData(data);

          // Redirect to dashboard
          this.router.navigate(['/products']);
        },
        (error) => {
          this.handleError(error);
        }
      );
    }
  }

  handleError(error: any) {
    let message: string = "Une erreur a eu lieu lors de l'authentification";

    switch (error.status) {
      case 401:
        // Unauthorized : When the login/password are not correct
        message = 'Invalid username or password';
        break;

      case 403:
        // Forbidden : when the user is disabled
        message = 'Forbidden';
        break;
    }

    console.log(message);

    this._alert.error({
      title: 'Oops...',
      showConfirmButton: false,
      timer: 1500,
      text: message,
    });
  }

  ngOnInit(): void {
    this.body.classList.add('login-page');
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  ngOnDestroy(): void {
    this.body.classList.remove('login-page');
  }
}
