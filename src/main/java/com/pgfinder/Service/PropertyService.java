package com.pgfinder.Service;

import com.pgfinder.Model.Property;
import com.pgfinder.Model.User;
import com.pgfinder.Repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserService userService;

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    public List<Property> searchProperties(String city, Integer numRooms, BigDecimal minRent, BigDecimal maxRent, Date availableDate) {
        return propertyRepository.findByCityContainingAndNumRoomsGreaterThanEqualAndRentGreaterThanEqualAndRentLessThanEqualAndAvailableDateGreaterThanEqual(city, numRooms, minRent, maxRent, availableDate);
    }

    public List<Property> getPropertiesByOwnerEmail(String email) {
        User owner = userService.getUserByEmail(email);
        return propertyRepository.findByOwner(owner);
    }

     // Update operation
     public Property updateProperty(Long id, Property updatedProperty) {
        Optional<Property> existingProperty = propertyRepository.findById(id);
        if (existingProperty.isPresent()) {
            Property property = existingProperty.get();
            property.setPropertyName(updatedProperty.getPropertyName());
            property.setAddress(updatedProperty.getAddress());
            property.setCity(updatedProperty.getCity());
            property.setState(updatedProperty.getState());
            property.setPincode(updatedProperty.getPincode());
            property.setDescription(updatedProperty.getDescription());
            property.setRent(updatedProperty.getRent());
            property.setNumRooms(updatedProperty.getNumRooms());
            property.setNumBathrooms(updatedProperty.getNumBathrooms());
            property.setOwner(updatedProperty.getOwner());
            property.setAvailableDate(updatedProperty.getAvailableDate());
            return propertyRepository.save(property);

        } else {
            return null; // Property not found
        }
    }


    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    // You can add more methods as needed for your application's functionality
}
