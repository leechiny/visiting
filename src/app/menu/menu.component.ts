import { Component, OnInit , OnDestroy} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit, OnDestroy {

  isUserLoggedIn = false;
  subscription!: Subscription;

  constructor(private router: Router,
    private authService: AuthService) { }

  ngOnInit(): void {
    this.subscription = this.authService.authenticationResultEvent.subscribe(
      (result: any) => {
        if ( result ) {
          this.isUserLoggedIn = true;
        }
        else {
          this.isUserLoggedIn = false;
        }
      }
    );
  } 

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  navigateToVisiting() {
    this.router.navigate(['visit']);
  }

  navigateToReport() {
    this.router.navigate(['report']);
  }

  logout() {
    this.authService.logout();
    this.navigateToVisiting();
  }
}
