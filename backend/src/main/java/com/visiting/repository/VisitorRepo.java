package com.visiting.repository;

import com.visiting.model.Visitor;
import org.springframework.data.repository.CrudRepository;

public interface VisitorRepo extends CrudRepository<Visitor, Long> {

  Visitor findByHashNricIgnoreCase(String hashNRIC);

}
