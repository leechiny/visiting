import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { VisitComponent } from './visit/visit.component';
import { ReportComponent } from './report/report.component';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DigitalClockComponent } from './digital-clock/digital-clock.component';
import { NgxSpinnerModule } from "ngx-spinner";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthRouteGuardService } from './service/auth-route-guard.service';

const routes: Routes = [
  {path: 'visit', component: VisitComponent},
  {path: 'report', component: ReportComponent, canActivate: [AuthRouteGuardService] },
  {path: 'login', component: LoginComponent},
  {path: '', component: VisitComponent},
  {path: '404', component: PageNotFoundComponent},
  {path: '**', redirectTo: '/404'}
]

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    VisitComponent,
    ReportComponent,
    LoginComponent,
    DigitalClockComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgxSpinnerModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(routes),
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule { }
