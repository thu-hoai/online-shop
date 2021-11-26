import { Component, ViewChild } from '@angular/core';
import { Application } from '../../../model/app.model';
import { ApplicationsService } from '../../../services/apps.service';
import { Router } from '@angular/router';
import { DataService } from '../../../services/data.service';
import { SweetAlertService } from 'angular-sweetalert-service/js';

@Component({
  selector: 'app-apps',
  templateUrl: 'apps.component.html'
})
export class ApplicationsComponent {

  applications: Application[] = [];

  public constructor(private router: Router, private _dataService: DataService, private _applicationService: ApplicationsService, private _alert: SweetAlertService) {

  }

  ngOnInit() {
    this.loadApps();
  }

  loadApps() {
    this._applicationService.loadApps().subscribe(data => this.applications = data);
  }
  editApp(app: Application) {
    this._dataService.setData(app);
    this.router.navigate(['/applications/add']);
  }

  deleteApp(app: Application) {
    this._alert.confirm({
      showCloseButton: true,
      confirmButtonText:
        '<i class="fa fa-trash"></i> Supprimer',
      showCancelButton: false,
      confirmButtonColor: '#f86c6b',
      title: `Voulez-vous supprimer l'application « ${app.name}» ?`
    })
      .then((res) => {
        if (res.value) {
          this._applicationService.deleteApp(app.id).subscribe(() => {
            this._alert.info({ title: 'Succès !', text: `L'application « ${app.name}» a été bien supprimé`, showConfirmButton: false, timer: 1500 });
            this.loadApps();
          });
        }
      });
  }
}
