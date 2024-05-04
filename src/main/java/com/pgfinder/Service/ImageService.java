package com.pgfinder.Service;

import com.pgfinder.Model.Image;
import com.pgfinder.Model.Property;
import com.pgfinder.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    // method to get images by propertid
    // public List<Image> getImagesByPropertyId(Long propertyId) {
    // return imageRepository.findByPropertyId(propertyId);
    // }
    public List<Image> getImagesByProperty(Property property) {
        return imageRepository.findByProperty(property);
    }

    // update image by property
    public Image updateImageByProperty(Property property, Image updatedImage) {
        List<Image> images = imageRepository.findByProperty(property);
        if (images.size() > 0) {
            updatedImage.setImageId(images.get(0).getImageId());
            return imageRepository.save(updatedImage);
        } else {
            return null; // Image not found
        }
    }

    // Read operation
    public Image getImageById(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        return image.orElse(null);
    }

    // Update operation
    public Image updateImage(Long id, Image updatedImage) {
        Optional<Image> existingImage = imageRepository.findById(id);
        if (existingImage.isPresent()) {
            updatedImage.setImageId(id);
            return imageRepository.save(updatedImage);
        } else {
            return null; // Image not found
        }
    }

    // Delete operation
    public boolean deleteImage(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            imageRepository.deleteById(id);
            return true;
        } else {
            return false; // Image not found
        }
    }

    // Get all images
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    // method to add
    public Image addImage(Image image) {
        return imageRepository.save(image);
    }
}
