# What is this for?

Prior to the implementation of a nationwide contact tracing system in Singapore, all visitors to a building must have their particulars and contact information recorded. This creates a long queue at the entrance especially when there is a peak. Furthermore, the same information is solicited for every repeated entry.

This simple application is created to address some of these problems. The national identity number is used as an identifier. The first time the visitor enters the building the identity card number, name, and contact number need to be entered. Subsequent entries only need to enter the identity card number. The use of a barcode reader to scan the identity number virtually removes all manual keying in of information for repeated visitors.


# Technoloy stack

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 11.0.7.

The backend was implemented using spring boot.

# Notable Features

* No clear ID number is stored (uses a hash of the ID number for storage & searches)
* Generation of daily entry report in PDF (login required)
* Validation of identity number and phone number
* Allows for integraton to barcode scanner for ID number capture

# Screen shots
<img width="967" alt="Screenshot 2021-04-01 at 3 18 34 PM" src="https://user-images.githubusercontent.com/62245757/113378509-ca6f9b00-93a9-11eb-8cc4-6c8bbc71b7fb.png">

<img width="474" alt="Screenshot 2021-04-01 at 3 18 56 PM" src="https://user-images.githubusercontent.com/62245757/113378582-f1c66800-93a9-11eb-953e-9e057f263f8a.png">

<img width="967" alt="Screenshot 2021-04-01 at 3 18 34 PM" src="https://user-images.githubusercontent.com/62245757/113378587-f854df80-93a9-11eb-9ce2-8ec55170adce.png">

# Setup (Development)

## Frontend
npm update
ng serve

## Backend
mvn spring-boot:run

# Important notice

The default is using an in-memory database. The data stored in the backend will disapper with every restart. Please configure to use a persistent database of your choice!

SSL is not setup for the front/backend. Need to setup for SSL for production deployment!

The backend is using an in-memory authentication manager with the credentials hard-coded!

