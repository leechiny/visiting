import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';
import { NricValidator } from '../validators/nric.validator';
import { VisitComponent } from './visit.component';

describe('VisitComponent', () => {
  let component: VisitComponent;
  let fixture: ComponentFixture<VisitComponent>;
  const icValidator = NricValidator();
  const control = new FormControl('input');

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fail', () => {
    control.setValue(null);
    expect(icValidator(control)).toBeTruthy();
  });

  it('should fail', () => {
    control.setValue('T1111111111');
    expect(icValidator(control)).toBeTruthy();
  });

  it('should fail', () => {
    control.setValue('S8131933E');
    expect(icValidator(control)).toBeTruthy();
  });

  it('shoule pass', ()=> {
    control.setValue('S8944027J');
    expect(icValidator(control)).toBeNull();
  });

  it('shoule pass', ()=> {
    control.setValue('T5405910D');
    expect(icValidator(control)).toBeNull();
  });

  it('shoule pass', ()=> {
    control.setValue('F0096038K');
    expect(icValidator(control)).toBeNull();
  });

  it('shoule pass', ()=> {
    control.setValue('G6868152P');
    expect(icValidator(control)).toBeNull();
  });

});
