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

    // public Property saveProperty(Property property, String userEmail) {
    //     // Fetch the user from the database
    //     User user = userService.getUserByEmail(userEmail);

    //     // Associate the property with the user
    //     property.setUser(user);

    //     // Save the property
    //     return propertyRepository.save(property);
    // }
    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    // You can add more methods as needed for your application's functionality
}
