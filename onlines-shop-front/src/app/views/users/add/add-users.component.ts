import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Application } from '../../../model/app.model';
import { UsersService } from '../../../services/users.service';
import { Router } from '@angular/router';
import { ApplicationsService } from '../../../services/apps.service';
import { SweetAlertService } from 'angular-sweetalert-service/js';
import { DataService } from '../../../services/data.service';

@Component({
  selector: 'app-users',
  templateUrl: 'add-users.component.html'
})
export class AddUsersComponent {

  editMode: boolean = false;
  submitted: boolean = false;
  userForm: FormGroup;

  targetApps: Application[] = [];
  sourceApps: Application[] = [];

  constructor(private router: Router, private fb: FormBuilder,
    private _dataService: DataService, private _usersService: UsersService, private _alert: SweetAlertService) {

  }

  ngOnInit() {
    this.userForm = this.fb.group({
      id: [''],
      active: ['false'],
      lastName: ['', Validators.required],
      firstName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      department: ['', Validators.required]
    });

    this._applicationsService.loadApps().subscribe(data => {
      this.sourceApps = data;
      this.initForm();
    });
  }

  initForm() {
    let user = this._dataService.getData();

    if (user) {
      this.editMode = true;
      this.targetApps = user.applications;
      this.correctPickList();

      this.userForm.setValue({
        id: user.id,
        active: user.active,
        lastName: user.lastName,
        firstName: user.firstName,
        email: user.email,
        department: user.department
      });
    }
  }

  correctPickList() {
    let appIds: number[] = this.targetApps.map(app => app.id);

    this.sourceApps = this.sourceApps.filter(app => appIds.indexOf(app.id) < 0);
  }

  onFormSubmit() {
    this.submitted = true;

    if (this.userForm.valid) {
      this._usersService.storeUser(this.userForm.value, this.targetApps).subscribe(() => {
        this.router.navigate(['/users/list']);
      },(err)=>{
        this.handleError(err);
      }
    );
    }
  }

  get lastName() {
    return this.userForm.get('lastName');
  }

  get firstName() {
    return this.userForm.get('firstName');
  }

  get email() {
    return this.userForm.get('email');
  }

  get department() {
    return this.userForm.get('department');
  }
  handleError(error: any) {
    
    let message = "Une erreur a eu lieu lors de l'authentification";

    switch (error.status) {
      case 401:
        // Unauthorized : When the login/password are not correct
        message = "Le login ou le mot de passe est incorrect";
        break;

      case 403:
        // Forbidden : when the user is disabled
        message = "Votre compte est désactivé";
        break;
    }

    this._alert.error({
      title: 'Oops...',
      showConfirmButton: false,
      timer: 1500,
      text: message
    });
  }
}
