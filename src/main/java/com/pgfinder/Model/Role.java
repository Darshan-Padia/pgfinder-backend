package com.pgfinder.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_code")
    private String roleCode;

    public Role() {
        // Default constructor required by JPA and Jackson
    }

    // Constructor with roleCode argument
    public Role(String roleCode) {
        this.roleCode = roleCode;
    }
  

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    
}
