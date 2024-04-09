package com.pgfinder.pgfinder;

import jakarta.persistence.*;

@Entity
@Table(name = "Amenity")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private Long amenityId;

    @Column(name = "name", nullable = false)
    private String name;

    // Getters and setters
}
