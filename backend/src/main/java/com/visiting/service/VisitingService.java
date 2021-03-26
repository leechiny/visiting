package com.visiting.service;

import com.visiting.controller.VisitingController;
import com.visiting.model.Visitor;
import com.visiting.model.VisitorEntry;
import com.visiting.repository.VisitorEntryRepo;
import com.visiting.repository.VisitorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class VisitingService {

  private static final Logger logger = LoggerFactory.getLogger(VisitingService.class);

  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
  final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Autowired
  VisitorEntryRepo visitorEntryRepo;

  @Autowired
  VisitorRepo visitorRepo;

  public Visitor getVisitorByHash(String hash) {
    return visitorRepo.findByHashNricIgnoreCase(hash);
  }

  public List<VisitorEntry> getVisitForDate(Timestamp date, int pageNo, int pageSize) {

    Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("id"));

    // Start of day
    LocalDateTime startOfDay = date.toLocalDateTime().toLocalDate().atStartOfDay();
    Timestamp fromDate = Timestamp.valueOf(startOfDay);

    // End of date
    LocalDateTime endOfDate = startOfDay.toLocalDate().atTime(LocalTime.MAX);
    Timestamp tillDate = Timestamp.valueOf(endOfDate);

    Page<VisitorEntry> pagedResult = visitorEntryRepo.findAllVisitorEntryBetweenDate(fromDate, tillDate, paging);

    if(pagedResult.hasContent()) {
      return pagedResult.getContent();
    } else {
      return new ArrayList<VisitorEntry>();
    }

  }

  @Transactional
  public VisitorEntry createEntry(Visitor visitor) {

    Visitor dbVisitor = visitorRepo.findByHashNricIgnoreCase(visitor.getHashNric());

    // Check whether the visitor exists and if not create
    if ( dbVisitor == null ) {
      logger.debug("creating visitor");
      dbVisitor = visitorRepo.save(visitor);
    }
    // Check whether any changes and if yes update visitor
    else {
      logger.debug("existing visitor");
      if ( !dbVisitor.equals(visitor) ) {
        logger.debug("changes to visitor details");
        dbVisitor.setName(visitor.getName());
        dbVisitor.setMaskedNric(visitor.getMaskedNric());
        visitorRepo.save(dbVisitor);
      }
    }

    logger.debug("creating entry for visitor");
    // Create an entry
    VisitorEntry entry = new VisitorEntry();
    entry.setVisitor(dbVisitor);
    entry.setTimestamp(new Timestamp(System.currentTimeMillis()));
    visitorEntryRepo.save(entry);

    return entry;
  }

}
