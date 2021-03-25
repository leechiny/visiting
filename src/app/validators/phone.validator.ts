import { ValidatorFn, AbstractControl } from "@angular/forms";

export function PhoneNumberValidator(): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {

    let phoneNo: string | null = control.value;

    if ( phoneNo === null || phoneNo === undefined ) {
      return { invalidPhoneNo: true }
    }

    phoneNo = phoneNo.trim().toUpperCase();
    if ( phoneNo.length !== 8 ) {
      return { invalidPhoneNo: true}
    }

    if ( isNaN( Number(phoneNo) ) ) {
      return { invalidPhoneNo: true};
    }

    if ( phoneNo.charAt(0) !== '6' && phoneNo.charAt(0) !== '8' && phoneNo.charAt(0) !== '9') {
      return { invalidPhoneNo: true};
    }

    return null;
  };
}
