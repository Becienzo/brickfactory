package com.caci.brickfactory.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;


import com.caci.brickfactory.model.Ord;

import com.caci.brickfactory.service.OrdService;

@RestController
public class OrderController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    private OrdService ordService;

    @GetMapping("/orders")
    public ResponseEntity<Iterable<Ord>> getAllOrders() {
        try {
            return ResponseEntity.ok()
                    .location((new URI("/orders")))
                    .body(ordService.findAll());
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/orders/{ordRef}")
    public ResponseEntity<?> getOrd(@PathVariable Long ordRef) throws Exception {
        return ordService.findById(ordRef)
                .map(ord -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/orders/" + ord.getOrdRef()))
                                .body(ord);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody Ord ord) {
        logger.info("Received Order: BrickAmount: " + ord.getAmountOfBrick());
        
        if(ord.getAmountOfBrick()>0){
            Ord newOrd = ordService.save(ord);
            try {
                return ResponseEntity
                        .created(new URI("/orders/" + newOrd.getOrdRef()))
                        .body(newOrd);
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
         

        
    }

    

}
