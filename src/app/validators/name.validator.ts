import { AbstractControl, ValidatorFn } from "@angular/forms";

export function NameValidator(): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    let name: string | null = control.value;

    if ( name === null || name === undefined ) {
      return { invalidName: true };
    }

    if ( name.trim().length == 0 ) {
      return { invalidName: true};
    }

    return null;
  }
}
