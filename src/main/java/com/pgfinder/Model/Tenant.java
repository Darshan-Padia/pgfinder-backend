package com.pgfinder.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Tenant")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Long tenantId;

    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



    // Other fields specific to Tenant

    @OneToMany(mappedBy = "tenant")
    private List<Rating> ratings;


    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    // Getters and setters
}
