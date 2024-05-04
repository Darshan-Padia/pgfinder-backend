package com.pgfinder.Controller;

import com.pgfinder.Model.Owner;
import com.pgfinder.Service.OwnerService;
import com.pgfinder.customannotions.AuthRequired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public ResponseEntity<List<Owner>> getAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        return new ResponseEntity<>(owners, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @AuthRequired
    public ResponseEntity<Owner> getOwnerById(@PathVariable("id") Long id) {
        Owner owner = ownerService.getOwnerById(id);
        if (owner != null) {
            return new ResponseEntity<>(owner, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // @GetMapping("/me")
    // @AuthRequired
    // public ResponseEntity<Owner> getOwner(
    //     @CookieValue(value = "email", defaultValue = "email") String email) {
    //         User user = userRepository.findByEmail(email);


    @PostMapping
    public ResponseEntity<Owner> saveOwner(@RequestBody Owner owner) {
        Owner savedOwner = ownerService.saveOwner(owner);
        return new ResponseEntity<>(savedOwner, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable("id") Long id) {
        ownerService.deleteOwner(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
