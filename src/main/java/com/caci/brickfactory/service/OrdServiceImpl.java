package com.caci.brickfactory.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.caci.brickfactory.model.Ord;
import com.caci.brickfactory.repository.OrdRepository;

import org.springframework.stereotype.Service;

@Service
public class OrdServiceImpl implements OrdService {

    private OrdRepository ordRepository;

    public OrdServiceImpl(OrdRepository ordRepository) {
        this.ordRepository = ordRepository;
    }

    @Override
    public Optional<Ord> findById(Long id) {

        return ordRepository.findById(id);
    }

    @Override
    public List<Ord> findAll() {
        List<Ord> result = StreamSupport.stream(ordRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Ord save(Ord ord) {
        ord = ordRepository.save(ord);
        // Set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newOrdUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{amountOfBrick}")
                .buildAndExpand(ord.getAmountOfBrick())
                .toUri();
        responseHeaders.setLocation(newOrdUri);
        return ord;
    }

   

}
