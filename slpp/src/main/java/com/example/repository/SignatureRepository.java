package com.example.repository;


import com.example.model.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {

    boolean existsByPetitionIdAndUserId(Long petitionId, Long userId);

    @Query("SELECT COUNT(s) FROM Signature s WHERE s.petition.id = :petitionId")
    Long countByPetitionId(@Param("petitionId") Long petitionId);

    List<Signature> findByPetitionId(Long petitionId);

    List<Signature> findByUserId(Long userId);

    @Query("SELECT s FROM Signature s JOIN FETCH s.user JOIN FETCH s.petition WHERE s.petition.id = :petitionId")
    List<Signature> findByPetitionIdWithUserDetails(@Param("petitionId") Long petitionId);

    void deleteByPetitionId(Long petitionId);
}