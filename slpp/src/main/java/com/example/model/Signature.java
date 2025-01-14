package com.example.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "signatures",
        uniqueConstraints = @UniqueConstraint(columnNames = {"petition_id", "user_id"}))
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petition_id", nullable = false)
    private Petition petition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "signed_at", nullable = false)
    private LocalDateTime signedAt;

    @PrePersist
    protected void onCreate() {
        signedAt = LocalDateTime.now();
    }
}