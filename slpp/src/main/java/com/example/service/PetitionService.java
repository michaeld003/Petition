package com.example.service;

import com.example.exception.DuplicateSignatureException;
import com.example.exception.PetitionClosedException;
import com.example.exception.PetitionNotFoundException;
import com.example.model.Petition;
import com.example.model.PetitionStatus;
import com.example.model.User;
import com.example.repository.PetitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PetitionService {
    private final PetitionRepository petitionRepository;
    private static int SIGNATURE_THRESHOLD = 100; // Default value

    public PetitionService(PetitionRepository petitionRepository) {
        this.petitionRepository = petitionRepository;
    }

    /**
     * Find all petitions
     */
    public List<Petition> findAll() {
        return petitionRepository.findAll();
    }

    /**
     * Find petitions by status
     */
    public List<Petition> findByStatus(PetitionStatus status) {
        return petitionRepository.findByStatus(status);
    }

    /**
     * Find petition by ID
     */
    public Petition findById(Long id) {
        return petitionRepository.findById(id)
                .orElseThrow(() -> new PetitionNotFoundException("Petition not found with id: " + id));
    }

    /**
     * Create a new petition
     */
    public Petition createPetition(Petition petition, User creator) {
        petition.setCreator(creator);
        petition.setStatus(PetitionStatus.OPEN);
        petition.setCreatedAt(LocalDateTime.now());
        return petitionRepository.save(petition);
    }

    /**
     * Sign a petition
     */
    public void signPetition(Long petitionId, User signer) {
        Petition petition = petitionRepository.findByIdWithSignatures(petitionId)
                .orElseThrow(() -> new PetitionNotFoundException("Petition not found with id: " + petitionId));

        if (petition.getStatus() == PetitionStatus.CLOSED) {
            throw new PetitionClosedException("Cannot sign closed petition");
        }

        if (petition.getSignatures().contains(signer)) {
            throw new DuplicateSignatureException("User has already signed this petition");
        }

        petition.getSignatures().add(signer);
        petitionRepository.save(petition);
    }

    /**
     * Get signature count for a petition
     */
    public Long getSignatureCount(Long petitionId) {
        return petitionRepository.countSignatures(petitionId);
    }

    /**
     * Add committee response and close petition
     */
    public void respondToPetition(Long petitionId, String response) {
        Petition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new PetitionNotFoundException("Petition not found with id: " + petitionId));

        petition.setResponse(response);
        petition.setStatus(PetitionStatus.CLOSED);
        petition.setClosedAt(LocalDateTime.now());
        petitionRepository.save(petition);
    }

    /**
     * Update signature threshold
     */
    public static void setSignatureThreshold(int threshold) {
        if (threshold < 1) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }
        SIGNATURE_THRESHOLD = threshold;
    }

    /**
     * Get current signature threshold
     */
    public static int getSignatureThreshold() {
        return SIGNATURE_THRESHOLD;
    }

    /**
     * Check if petition has met signature threshold
     */
    public boolean hasMetThreshold(Long petitionId) {
        return getSignatureCount(petitionId) >= SIGNATURE_THRESHOLD;
    }

    /**
     * Find petitions created by a specific user
     */
    public List<Petition> findByCreatorId(Long creatorId) {
        return petitionRepository.findByCreatorId(creatorId);
    }
}