import { Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'app-digital-clock',
  templateUrl: './digital-clock.component.html',
  styleUrls: ['./digital-clock.component.css']
})
export class DigitalClockComponent implements OnInit, OnDestroy{

  static readonly daysOfWeek = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturaday'];
  id: any;
  date!: Date;
  day!: string;
  ampm!: string;
  hour!: any;
  minute!: any;
  second!: any;

  constructor() { }

  ngOnInit(): void {
    this.id = setInterval(()=>{
      this.date = new Date();
      this.updateDate(this.date);
    }, 1000);
  }

  ngOnDestroy(): void {
    clearInterval(this.id);
  }

  updateDate(date: Date){
    const hours = date.getHours();

    this.ampm = hours >= 12 ? 'PM' : 'AM';

    this.hour = hours % 12;
    this.hour = this.hour ? this.hour : 12;
    this.hour = this.hour < 12 ? '0' + this.hour : this.hour;

    const minutes = date.getMinutes();
    this.minute = minutes < 10 ? '0' + minutes : minutes.toString();

    const seconds = date.getSeconds();
    this.second = seconds < 10 ? '0' + seconds : seconds.toString();

    this.day = DigitalClockComponent.daysOfWeek[this.date.getDay()];
  }
}
