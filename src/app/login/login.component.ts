import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs';
import { AuthService } from '../service/auth.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy{

  name = "";
  password = "";
  subscription!: Subscription;
  @ViewChild('errordialog') errordialog: any;

  constructor(private authService: AuthService,
    private route: Router,
    private activatedRoute: ActivatedRoute,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,) { }

  ngOnInit(): void {
    this.subscription = this.authService.authenticationResultEvent.subscribe(
      ( result: any) => {
        this.spinner.hide();
        console.log("result " + result);
        if ( result ) {
          const url = this.activatedRoute.snapshot.queryParams['requested'];
          this.route.navigateByUrl(url);
        }
        else {
          this.modalService.open(this.errordialog, {centered: true, ariaLabelledBy: 'modal-basic-title'})
        }
      }
    );
    this.authService.checkIfAlreadyAuthenticated();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  onSubmit() {
    this.spinner.show()
    this.authService.authenticate(this.name, this.password);
  }
  
}
