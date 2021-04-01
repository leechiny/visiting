# What is this for?

Prior to the implementation of nation wide contact tracing system in Singapore, all visitors to a buidling must have their particulars and contact information recorded. This creates a long queue at the entrace especially when where is a peak. Furthermore the same information is solicited for every repeated entry.

This simple application is created to address some of these problems. The national identity number is used as a identifer. The first time the visitor enter the building the identity card number, name and contact number needs to be entered. Subsequent entry only need to enter the identify card number. The use of a barcode reader to scan the identity number virtually remove all manual keying in of information for repeated visitors.


# Technoloy stack

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 11.0.7.

The backend was implemented using spring boot.

# Notable Features

* No clear ID number is stored (uses a hash of the ID number for storage & searches)
* Generation of daily entry report in PDF (login required)
* Validation of identity number and phone number
* Allows for integraton to barcode scanner for ID number capture

# Screen shots


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


# License
