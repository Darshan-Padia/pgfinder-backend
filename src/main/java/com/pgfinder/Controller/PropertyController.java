package com.pgfinder.Controller;

import com.pgfinder.Model.Property;
import com.pgfinder.Service.PropertyService;
import com.pgfinder.customannotions.AuthRequired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
// @CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")

@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.getPropertyById(id);
        return property.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(required = false, defaultValue = "") String city,
            @RequestParam(required = false, defaultValue = "1") Integer numRooms,
            @RequestParam(required = false, defaultValue = "0") BigDecimal minRent,
            @RequestParam(required = false, defaultValue = "1000000") BigDecimal maxRent,
            @RequestParam(required = false, defaultValue = "2000-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date availableDate) {

        List<Property> properties;

        System.out.println("city numRooms minRent maxRent availableDate");
        System.out.println(city + " " + numRooms + " " + minRent + " " + maxRent + " " + availableDate);

        properties = propertyService.searchProperties(city, numRooms, minRent, maxRent, availableDate);

        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    // @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/add")
    @AuthRequired
    public ResponseEntity<Property> addProperty(

            @RequestBody Property property) {

        Property newProperty = propertyService.saveProperty(property);
        return new ResponseEntity<>(newProperty, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
