import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  isUserLoggedIn = false;

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  navigateToVisiting() {
    this.router.navigate(['visit']);
  }

  navigateToReport() {
    this.router.navigate(['report']);
  }

  logout() {
    // TODO:
  }
}
