import { ValidatorFn, AbstractControl } from "@angular/forms";

export function NricValidator(): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    const multiples = [2, 7, 6, 5, 4, 3, 2 ];
    const stChecksum = [ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'Z', 'J' ];
    const fgChecksum = [ 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'T', 'U', 'W', 'X' ];

    let nric: string | null = control.value;

    if ( nric === null || nric === undefined ) {
      return { invalidNRIC: true }
    }

    nric = nric.trim().toUpperCase();
    if ( nric.length !== 9 ) {
      return { invalidNRIC: true}
    }

    let prefix: string = nric.charAt(0);
    if  ( prefix !== 'S' && prefix !== 'F' && prefix !== 'T' && prefix !== 'G' )  {
      return { invalidNRIC: true};
    }

    let num = Number( nric.substring(1, 8) );
    if ( isNaN(num) ) {
      return { invalidNRIC: true};
    }

    let result = 0;
    if ( prefix === 'T' || prefix === 'G' ) {
      result = 4;
    }

    for(let i=0; i<7; i++ ) {
      result += multiples[i] * Number( nric.substring(i+1, i+2) );
    }
    result = result % 11;

    let digit = '';
    if ( prefix === 'S' || prefix === 'T') {
      digit = stChecksum[10 - result];
    }
    else {
      digit = fgChecksum[10 - result];
    }

    if ( digit !== nric.charAt(8)) {
      return { invalidNRIC: true};
    }

    return null;
  };
}
