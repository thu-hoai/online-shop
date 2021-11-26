import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { trigger, style, animate, transition } from '@angular/animations';
import { TokenService } from '../../services/token.service';
import { Router, ActivatedRoute } from '@angular/router';
import { VerificationToken } from '../../model/token.model';
import 'rxjs/operators';
import { finalize } from 'rxjs/operators';;

@Component({
  selector: 'app-dashboard',
  templateUrl: 'activate-account.component.html',
  animations: [
    trigger('enterAnimation', [
      transition(':enter', [
        style({ transform: 'translateY(10%)', opacity: 0 }),
        animate('500ms', style({ transform: 'translateY(0)', opacity: 1 }))
      ]),
      transition(':leave', [
        style({ opacity: 1 }),
        animate('500ms', style({ opacity: 0 }))
      ])
    ])
  ],
})
export class ActivateAccountComponent {

  loading: boolean = true;
  success: boolean = false;
  expired: boolean = false;
  badLink: boolean = false;
  error: boolean = false;

  verificationToken: VerificationToken;
  token: string;

  body: any;
  sub: any;

  passwordForm: FormGroup;

  public constructor(private fb: FormBuilder, private router: Router, private route: ActivatedRoute, private _tokenService: TokenService) {
    this.body = document.getElementsByTagName('body')[0];
  }

  ngOnInit(): void {

    this.sub = this.route.params.subscribe(params => {
      this.token = params['token'];
      this._tokenService.verifyToken(this.token).pipe(finalize(() => {
        this.loading = false;
      })).subscribe(data => {
        this.verificationToken = data;
        this.initPasswordForm();
      }, error => {
        this.handleError(error);
      });
    });

    this.body.classList.add('login-page');
  }

  handleError(error: any) {
    switch (error.status) {
      case 400:
        // Expired link
        this.verificationToken = {
          email: error.error.detail,
          token: this.token
        };
        this.expired = true;
        break;

      case 403:
        // Bad link
        this.badLink = true;
        break;

      default:
        this.error = true;
        break;
    }
  }

  initPasswordForm(): void {
    this.passwordForm = this.fb.group({
      token: this.token,
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  }

  onFormSubmit() {
    let form: any = this.passwordForm.value;
    this.passwordForm.markAsPristine();

    if (this.matchingPasswords()) {
      // Save password
      this._tokenService.setPassword(form.token, form.password).subscribe(() => {
        this.success = true;
      });
    }
  }

  matchingPasswords(): boolean {
    return this.passwordForm.value.password === this.passwordForm.value.confirmPassword;
  }

  ngOnDestroy(): void {
    this.body.classList.remove('login-page');
  }

  resendActivationLink() {
    this._tokenService.resendActivationLink(this.token).subscribe(data => {
      this.success = true;
    });
  }

  savePassword() {
    this.success = true;
  }
}