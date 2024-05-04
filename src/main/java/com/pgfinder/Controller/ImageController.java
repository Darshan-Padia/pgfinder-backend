package com.pgfinder.Controller;

import com.pgfinder.Model.Image;
import com.pgfinder.Model.Property;
import com.pgfinder.Service.ImageService;
import com.pgfinder.Service.PropertyService;
import com.pgfinder.customannotions.AuthRequired;
import com.pgfinder.dto.ImageRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;
    private final PropertyService propertyService;


    @Autowired
    public ImageController(ImageService imageService,  PropertyService propertyService) {
        this.imageService = imageService;
        this.propertyService = propertyService;
    }


    // get images by property id
    /*
     *   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    // store image  path
    @Column(name = "image", nullable = false)
    private String image;

     */
    @GetMapping("/{propertyId}")
    public ResponseEntity<?> getImagesByPropertyId(@PathVariable String propertyId) {
        System.out.println("inside getImagesByPropertyId");
        System.out.println("propertyId: " + propertyId);
        Optional<Property> propertyOptional = propertyService.getPropertyById(Long.parseLong(propertyId));
        System.out.println("propertyOptional: " + propertyOptional);
        if (propertyOptional.isPresent()) {
            List<Image> images = imageService.getImagesByProperty(propertyOptional.get());
            // printing each image
            for (Image image : images) {
                System.out.println("image: " + image.getImage());
            }
            return new ResponseEntity<>(images, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Property with id " + propertyId + " not found", HttpStatus.NOT_FOUND);
        }
    }

  // Endpoint to find images by propertyId
    @PostMapping
public ResponseEntity<?> addImages(@RequestBody List<ImageRequest> imageRequests) {
    for (ImageRequest imageRequest : imageRequests) {
        Optional<Property> propertyOptional = propertyService.getPropertyById(Long.parseLong(imageRequest.getPropertyId()));
        if (propertyOptional.isPresent()) {
            Image image = new Image();
            image.setImage(imageRequest.getPath());
            image.setProperty(propertyOptional.get());
            imageService.addImage(image);
        } else {
            return new ResponseEntity<>("Property with id " + imageRequest.getPropertyId() + " not found", HttpStatus.NOT_FOUND);
        }
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
}
}
