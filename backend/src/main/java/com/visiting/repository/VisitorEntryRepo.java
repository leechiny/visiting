package com.visiting.repository;


import com.visiting.model.VisitorEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface VisitorEntryRepo extends CrudRepository<VisitorEntry, Long> {

  @Query("select v from VisitorEntry v where v.timestamp >= :fromDt and v.timestamp < :toDt")
  Page<VisitorEntry> findAllVisitorEntryBetweenDate(@Param("fromDt") Timestamp fromDate, @Param("toDt") Timestamp tillDate, Pageable pageable);

}
