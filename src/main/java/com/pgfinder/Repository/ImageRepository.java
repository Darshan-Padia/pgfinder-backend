package com.pgfinder.Repository;

import com.pgfinder.Model.Image;
import com.pgfinder.Model.Property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // add image paths
    // List<Image> findByPropertyId(Long propertyId);
    List<Image> findByProperty(Property property);
    
    }
