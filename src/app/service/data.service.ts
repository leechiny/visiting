import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { VisitingEntry } from '../model/visiting-entry';
import { Visitor } from '../model/visitor';
import * as CryptoJS from 'crypto-js';
import { HttpClient } from '@angular/common/http';
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


}

class _Visitor {
  name!: string;
  hashNric!: string;
  maskedNric!: string;
  phoneNo!: string;
}
