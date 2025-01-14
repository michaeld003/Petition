package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<com.example.model.User, Long> {
//    Optional<com.example.model.User> findByEmail(String email);
//    Optional<com.example.model.User> findByBioId(String bioId);
//    boolean existsByEmail(String email);
//    boolean existsByBioId(String bioId);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByBioId(String bioId);
    Optional<User> findByBioId(String bioId);


}


