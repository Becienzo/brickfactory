package com.caci.brickfactory.service;

import java.util.List;
import java.util.Optional;



import com.caci.brickfactory.model.Ord;

public interface OrdService {
    Optional<Ord> findById(Long id);
    List<Ord> findAll();
    Ord save(Ord ord);
    

}
