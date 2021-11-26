import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { navItems } from '../../_nav';
import { AuthenticationService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html'
})
export class DefaultLayoutComponent {
  public navItems = navItems;
  public sidebarMinimized = true;
  private changes: MutationObserver;
  public element: HTMLElement = document.body;

  currentUser:any;

  constructor(private router: Router, private _authService: AuthenticationService) {

    this.changes = new MutationObserver((mutations) => {
      this.sidebarMinimized = document.body.classList.contains('sidebar-minimized')
    });

    this.changes.observe(<Element>this.element, {
      attributes: true
    });
  }

  ngOnInit() {
    this.currentUser = this._authService.getCurrentUser();
  }

  getDefaultImage($event) {
    if (this.currentUser) {
      $event.target.src = `https://ui-avatars.com/api/?name=${this.currentUser.firstName}+${this.currentUser.lastName}`;
    }
  }

  logOut() {
    this._authService.logout();
    this.router.navigate(['/login']);
  }
}
