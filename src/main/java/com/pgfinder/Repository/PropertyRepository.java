package com.pgfinder.Repository;

import com.pgfinder.Model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*
 * 
 * GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(required = false, defaultValue = "") String city,
            @RequestParam(required = false, defaultValue = "1") Integer numRooms,
            @RequestParam(required = false, defaultValue = "0") BigDecimal minRent,
            @RequestParam(required = false, defaultValue = "1000000") BigDecimal maxRent,
            @RequestParam(required = false, defaultValue = "2000-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date availableDate) {

 */
                
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

        List<Property> findByCityContainingAndNumRoomsGreaterThanEqualAndRentGreaterThanEqualAndRentLessThanEqualAndAvailableDateGreaterThanEqual(
                        String city,
                        Integer numRooms,
                        BigDecimal minRent,
                        BigDecimal maxRent,
                        Date availableDate);

}
