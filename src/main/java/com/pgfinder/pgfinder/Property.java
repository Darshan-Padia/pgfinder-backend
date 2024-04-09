package com.pgfinder.pgfinder;

// note that -> java.persistence is changed to jakarta.persistence!
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import java.util.List;

@Entity
@Table(name = "Property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "pincode", nullable = false)
    private String pincode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "property")
    private List<Rating> ratings;

    @Column(name = "rent", nullable = false)
    private BigDecimal rent;

    @Column(name = "num_rooms", nullable = false)
    private int numRooms;

    @Column(name = "num_bathrooms", nullable = false)
    private int numBathrooms;

    @Column(name = "available_date")
    private Date availableDate;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

}
