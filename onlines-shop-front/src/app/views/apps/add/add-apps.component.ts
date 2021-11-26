import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApplicationsService } from '../../../services/apps.service';
import { DataService } from '../../../services/data.service';

@Component({
  selector: 'app-apps',
  templateUrl: 'add-apps.component.html'
})
export class AddApplicationsComponent {

  editMode: boolean = false;
  submitted: boolean = false;
  appForm: FormGroup;
  appId: string;

  constructor(private router: Router, private fb: FormBuilder,
    private _dataService: DataService, private _applicationsService: ApplicationsService) { }

  ngOnInit() {
    this.appForm = this.fb.group({
      id: '',
      name: ['', Validators.required],
      description: ''
    });

    this.initForm();
  }

  initForm() {
    let data:any = this._dataService.getData();
    this.appId = data.appId;

    if (data) {
      this.editMode = true;
      delete data.appId;
      this.appForm.setValue(data);
    }
  }

  onFormSubmit() {
    this.submitted = true;
    
    if (this.appForm.valid) {
      this._applicationsService.storeApp(this.appForm.value).subscribe(() => {
        this.router.navigate(['/applications/list']);
      });
    }
  }

  get name() {
    return this.appForm.get('name');
  }
}
