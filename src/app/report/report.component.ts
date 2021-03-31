import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbCalendar } from '@ng-bootstrap/ng-bootstrap';
import { DataService } from '../service/data.service';
import { map } from 'rxjs/operators';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {


  date!: { year: number, month: number, day: number };

  @ViewChild('errordialog') errordialog: any; 

  constructor(private calendar: NgbCalendar,
    private spinner: NgxSpinnerService,
    private modalService: NgbModal,
    private dataService: DataService) { }

  ngOnInit(): void {
    this.date = this.calendar.getToday();
  }

  onClick() {

    console.log('selected : ' + this.date.year + '-' + this.date.month + '-' + this.date.day)
    this.spinner.show();
    this.dataService.downloadReport(this.date.year + '-' + this.date.month + '-' + this.date.day).pipe(
      map(data => {

        let blob = new Blob([data], {
          type: 'application/pdf'
        });

        var fileURL: any = URL.createObjectURL(blob);
        var a = document.createElement("a");
        a.href = fileURL;
        a.target = '_blank';
        a.click();

      })).subscribe(
        () => {
          this.spinner.hide();
        },
        (error) => {
          this.spinner.hide();
          this.modalService.open(this.errordialog, {centered: true, ariaLabelledBy: 'modal-basic-title'})
        }

      )
  }

}
