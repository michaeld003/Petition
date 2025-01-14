package com.example.controller;

import com.example.service.PetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/committee")
public class CommitteeController {
    private final PetitionService petitionService;

    public CommitteeController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    @PostMapping("/threshold")
    @PreAuthorize("hasRole('COMMITTEE')")
    public ResponseEntity<String> setThreshold(@RequestParam int threshold) {
        PetitionService.setSignatureThreshold(threshold);
        return ResponseEntity.ok("Threshold updated successfully");
    }

    @PostMapping("/respond/{id}")
    @PreAuthorize("hasRole('COMMITTEE')")
    public ResponseEntity<String> respondToPetition(
            @PathVariable Long id,
            @RequestBody String response) {
        petitionService.respondToPetition(id, response);
        return ResponseEntity.ok("Response added successfully");
    }
}