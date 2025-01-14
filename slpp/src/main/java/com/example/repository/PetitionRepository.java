package com.example.repository;

import com.example.model.Petition;
import com.example.model.PetitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
    List<Petition> findByStatus(PetitionStatus status);

    @Query("SELECT p FROM Petition p LEFT JOIN FETCH p.signatures WHERE p.id = ?1")
    Optional<Petition> findByIdWithSignatures(Long id);

    @Query("SELECT COUNT(s) FROM Petition p JOIN p.signatures s WHERE p.id = ?1")
    Long countSignatures(Long petitionId);

    List<Petition> findByCreatorId(Long creatorId);


    List<Petition> findByStatusOrderByCreatedAtDesc(PetitionStatus status);
}




