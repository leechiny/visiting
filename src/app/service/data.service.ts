import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { VisitingEntry } from '../model/visiting-entry';
import { Visitor } from '../model/visitor';
import * as CryptoJS from 'crypto-js';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }

  sanitiseVisitor(visitor: Visitor): _Visitor {
    let v = new _Visitor();

    v.name = visitor.name!;
    v.hashNric= CryptoJS.SHA256(visitor.nric!.toUpperCase()).toString().toUpperCase();
    v.maskedNric = '***' + visitor.nric?.substring(4).toUpperCase();
    v.phoneNo = visitor.phoneNo!

    return v;

  }

  createEntry(visitor: Visitor): Observable<VisitingEntry> {

    return this.http.post<VisitingEntry>(environment.restUrl + '/api/create', this.sanitiseVisitor(visitor));

  }

  queryVisitor(visitor: Visitor): Observable<Visitor> {
    const v = this.sanitiseVisitor(visitor);
    return this.http.get<Visitor>(environment.restUrl + '/api/query/' + v.hashNric );
  }

  downloadReport(date: string): Observable<any> {
    let headerOptions = new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/pdf',
    });
    let requestOptions = { headers: headerOptions, responseType: 'blob' as 'blob', withCredentials: true};
    return this.http.get(environment.restUrl + '/api/report/' + date, requestOptions);

  }

  validateUser(name: string, password: string): Observable<{result: string}> {
    const authData = btoa(`${name}:${password}`);
    const headers = new HttpHeaders().append( 'Authorization', 'Basic ' + authData);
    return this.http.get<{result: string}>(environment.restUrl + '/api/basicAuth/validate', { headers,  withCredentials: true } );
  }

  getRole(): Observable<{role: string}> {
    const headers = new HttpHeaders().append('X-Requested-With', 'XMLHttpRequest');
    return this.http.get<{role: string}>(environment.restUrl + '/api/currentUserRole', { headers, withCredentials: true} );
  }
  
  logout(): Observable<string> {
    return this.http.get<string>(environment.restUrl + '/api/logout', {withCredentials: true} );  
  }

}

class _Visitor {
  name!: string;
  hashNric!: string;
  maskedNric!: string;
  phoneNo!: string;
}
