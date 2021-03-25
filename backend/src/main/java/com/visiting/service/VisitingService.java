package com.visiting.service;

import com.visiting.controller.VisitingController;
import com.visiting.model.Visitor;
import com.visiting.model.VisitorEntry;
import com.visiting.repository.VisitorEntryRepo;
import com.visiting.repository.VisitorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
public class VisitingService {

  private static final Logger logger = LoggerFactory.getLogger(VisitingService.class);

  @Autowired
  VisitorEntryRepo visitorEntryRepo;

  @Autowired
  VisitorRepo visitorRepo;

  public Visitor getVisitorByHash(String hash) {
    return visitorRepo.findByHashNricIgnoreCase(hash);
  }

  @Transactional
  public VisitorEntry createEntry(Visitor visitor) {

    Visitor dbVisitor = visitorRepo.findByHashNricIgnoreCase(visitor.getHashNric());

    // Check whether the visitor exists and if not create
    if ( dbVisitor == null ) {
      logger.debug("creating visitor");
      visitorRepo.save(visitor);
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
    entry.setHashNRIC(visitor.getHashNric());
    entry.setTimestamp(new Timestamp(System.currentTimeMillis()));
    visitorEntryRepo.save(entry);

    return entry;
  }

}
