package com.caci.brickfactory.repository;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.caci.brickfactory.model.Ord;

@Repository
public interface OrdRepository extends CrudRepository<Ord, Long> {

}
