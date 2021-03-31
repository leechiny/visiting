import { EventEmitter, Injectable } from '@angular/core';
import { DataService } from './data.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticated = false;

  authenticationResultEvent = new EventEmitter<boolean>();

  role!: string;

  constructor(private dataService: DataService) { }

  authenticate(name: string, password: string) {
    this.dataService.validateUser(name, password).subscribe(
      (next) => {
        this.isAuthenticated = true;
        this.authenticationResultEvent.emit(true);
      },
      (error) => {
        console.log('error' + error);
        this.authenticationResultEvent.emit(false);
        this.isAuthenticated = false;
      }
    );
  }

  checkIfAlreadyAuthenticated() {
    this.dataService.getRole().subscribe(
      (next) => {
        if ( next.role !== '' ) {
          this.role = next.role;
          this.isAuthenticated = true;
          this.authenticationResultEvent.emit(true);
        }
      }
    )
  }

  logout() {
    this.dataService.logout().subscribe();
    this.isAuthenticated = false;
    this.authenticationResultEvent.emit(false);
  }


}
