package com.visiting.controller;

import com.visiting.model.Visitor;
import com.visiting.model.VisitorEntry;
import com.visiting.service.VisitingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "http://localhost:4200")
public class VisitingController {

  private static final Logger logger = LoggerFactory.getLogger(VisitingController.class);

  @Autowired
  VisitingService visitingService;

  @PostMapping(value = "/create")
  public ResponseEntity<Object> createEntry(@RequestBody Visitor visitor) throws InterruptedException {
    logger.debug("Calling create entry with visitor:\n" + visitor.toString());
    VisitorEntry entry = visitingService.createEntry(visitor);


    if (entry == null) {
      return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
    }

    return new ResponseEntity<>(entry, HttpStatus.CREATED);
  }

  @GetMapping(value = "/query/{nricHash}")
  public ResponseEntity<Object> query(@PathVariable String nricHash) {
    logger.debug("Getting the visitor with nric hash: " + nricHash);
    Visitor visitor = visitingService.getVisitorByHash(nricHash);

    if ( visitor == null ) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(visitor, HttpStatus.OK);
  }
}

