// User.java
package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "bio_id", unique = true, nullable = false)
    private String bioId;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.PETITIONER;

    public UserRole getRole() {
        return role != null ? role : UserRole.PETITIONER;
    }

    @OneToMany(mappedBy = "creator")
    private Set<Petition> createdPetitions;

    @ManyToMany
    @JoinTable(
            name = "petition_signatures",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "petition_id")
    )
    private Set<Petition> signedPetitions;
}

