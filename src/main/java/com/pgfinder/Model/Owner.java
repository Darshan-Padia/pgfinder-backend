package com.pgfinder.Model;

// note that -> java.persistence is changed to jakarta.persistence!
import jakarta.persistence.*;

@Entity
@Table(name = "Owner")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long ownerId;

   
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
