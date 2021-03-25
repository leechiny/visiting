import { Component, ElementRef, Input, OnInit, ViewChild  } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import { Visitor } from '../model/visitor';
import { DataService } from '../service/data.service';
import { NameValidator } from '../validators/name.validator';
import { NricValidator } from '../validators/nric.validator';
import { PhoneNumberValidator } from '../validators/phone.validator';

@Component({
  selector: 'app-visit',
  templateUrl: './visit.component.html',
  styleUrls: ['./visit.component.css']
})
export class VisitComponent implements OnInit {

  @Input()
  visitor!: Visitor;

  visitForm!: FormGroup;

  entryTime!: string;

  @ViewChild('okdialog') okdialog: any;
  @ViewChild('errordialog') errordialog: any;
  @ViewChild('nricInput') nric!: ElementRef;
  @ViewChild('entry') entry!:ElementRef;

  okdialogReference!: any;

  constructor(private formBuilder: FormBuilder,
    private dataService: DataService,
    private spinner: NgxSpinnerService,
    private modalService: NgbModal,
    private config: NgbModalConfig) { }

  ngOnInit(): void {
    this.config.backdrop = 'static';
    this.config.keyboard = false;
    this.initialiseForm();
  }

  onNricChange() {
    if ( this.visitForm.controls['nric'].valid && this.visitForm.controls['nric'].dirty ) {
      this.dataService.queryVisitor(this.visitor).subscribe(
        (next) => {
          this.visitor.name = next.name;
          this.visitor.phoneNo = next.phoneNo;
          this.entry.nativeElement.focus();
        },
        (error) => {
        }
        
      );
    }
  }

  initialiseForm() {

    this.visitor = new Visitor();
    this.visitForm = this.formBuilder.group({
      nric: new FormControl('', [Validators.required, NricValidator()] ),
      phoneNo: new FormControl('', [Validators.required, PhoneNumberValidator()]),
      name: new FormControl('', [Validators.required, NameValidator()])
    });
    if ( this.nric ) {
      this.nric.nativeElement.focus();
    }
  }

  ngAfterViewInit() {
    this.nric.nativeElement.focus();
  }

  onSubmit() {
    if ( this.visitForm.valid ) {
      this.spinner.show();
      this.dataService.createEntry(this.visitor).subscribe(
        (entry)=>{
          this.spinner.hide();
          this.entryTime = entry.timestamp;
          this.okdialogReference = this.modalService.open(this.okdialog, {centered: true, ariaLabelledBy: 'modal-basic-title'})
          setTimeout( ()=>{
            this.okdialogReference.close();
            this.initialiseForm();
          }, 2000);
        },
        (error) => {
          this.spinner.hide();
          this.modalService.open(this.errordialog, {centered: true, ariaLabelledBy: 'modal-basic-title'})
        }
      );
    }
  }

  onClear() {
    this.initialiseForm();
  }

}


