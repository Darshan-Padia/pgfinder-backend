package com.pgfinder.Controller;

import com.pgfinder.Model.Image;
import com.pgfinder.Model.Property;
import com.pgfinder.Model.User;
import com.pgfinder.Repository.ImageRepository;
import com.pgfinder.Service.PropertyService;
import com.pgfinder.Service.UserService;
import com.pgfinder.customannotions.AuthRequired;

import io.jsonwebtoken.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
// @CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")

@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        System.out.println("inside the get prooperty by id ");
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

    /*
     const handleUpdateProperty = async () => {
    try {
      const response = await axios.put(`/api/properties/${id}`, updatedProperty);
      console.log('Property updated successfully:', response.data);
      setProperty(updatedProperty);
      setEditMode(false);
    } catch (error) {
      console.error('Error updating property:', error);
    }
  };
     */

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(
            @PathVariable Long id,
            @RequestBody Property updatedProperty) {
        Property property = propertyService.updateProperty(id, updatedProperty);
        if (property != null) {
            return new ResponseEntity<>(property, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // to check if the property is owned by the prticular user or not  true or false

    @GetMapping("/check/{id}")
    @AuthRequired
    public ResponseEntity<Boolean> checkPropertyOwner(
            @CookieValue(value = "email", defaultValue = "no email") String email,
            @PathVariable Long id) {
                System.out.println("inside the check property owner");
                
        User owner = userService.getUserByEmail(email);
        Property property = propertyService.getPropertyById(id).orElse(null);
        if (property != null && property.getOwner().equals(owner)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    // get properties by owner_id column
    @GetMapping("/me")
    @AuthRequired
    public ResponseEntity<List<Property>> getPropertiesByOwner(
            @CookieValue(value = "email", defaultValue = "no email") String email) {
        List<Property> properties = propertyService.getPropertiesByOwnerEmail(email);
        if (properties != null) {
            return new ResponseEntity<>(properties, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/add")
    @AuthRequired
    public ResponseEntity<?> addProperty(
            @CookieValue(value = "email", defaultValue = "no email") String email,
            @RequestBody Property property) {
            Property newProperty =new  Property();
            newProperty.setPropertyName(property.getPropertyName());
            newProperty.setAddress(property.getAddress());
            newProperty.setCity(property.getCity());
            newProperty.setState(property.getState());
            newProperty.setPincode(property.getPincode());
            newProperty.setDescription(property.getDescription());
            newProperty.setRent(property.getRent());
            newProperty.setNumRooms(property.getNumRooms());
            newProperty.setNumBathrooms(property.getNumBathrooms());
            newProperty.setOwner(userService.getUserByEmail(email));
            newProperty.setAvailableDate(property.getAvailableDate());
            newProperty.setAvailable(property.isAvailable());
                
            propertyService.saveProperty(newProperty);
        
        return new ResponseEntity<>(newProperty, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
