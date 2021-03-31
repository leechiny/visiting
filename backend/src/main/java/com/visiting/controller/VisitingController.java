package com.visiting.controller;

import com.itextpdf.text.DocumentException;
import com.visiting.model.Visitor;
import com.visiting.model.VisitorEntry;
import com.visiting.service.ReportGenerator;
import com.visiting.service.VisitingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "http://localhost:4200")
public class VisitingController {

  private static final Logger logger = LoggerFactory.getLogger(VisitingController.class);
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");

  @Autowired
  VisitingService visitingService;

  @Autowired
  ReportGenerator reportGenerator;

  @GetMapping(value = "/report/{date}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String date, HttpServletRequest request) throws IOException, DocumentException, ParseException {
    logger.debug("Generating report for : {}", date);

    File report = reportGenerator.writeFile(new Timestamp( dateFormat.parse(date).getTime()));
    Resource resource = new UrlResource(report.toPath().toUri());

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(resource.getFile().toPath()))
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
      .body(resource);

  }

  @PostMapping(value = "/create")
  public ResponseEntity<Object> createEntry(@RequestBody Visitor visitor) throws InterruptedException, IOException, DocumentException {
    logger.debug("Calling create entry with visitor:\n{}", visitor.toString());
    VisitorEntry entry = visitingService.createEntry(visitor);


    if (entry == null) {
      return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
    }

    reportGenerator.writeFile(new Timestamp(System.currentTimeMillis()));

    return new ResponseEntity<>(entry, HttpStatus.CREATED);
  }

  @GetMapping(value = "/query/{nricHash}")
  public ResponseEntity<Object> query(@PathVariable String nricHash) {
    logger.debug("Getting the visitor with nric hash: {}", nricHash);
    Visitor visitor = visitingService.getVisitorByHash(nricHash);

    if ( visitor == null ) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(visitor, HttpStatus.OK);
  }


}

