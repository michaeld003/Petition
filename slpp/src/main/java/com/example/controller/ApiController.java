package com.example.controller;

import com.example.dto.PetitionDTO;
import com.example.model.Petition;
import com.example.model.PetitionStatus;
import com.example.service.PetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/slpp/petitions")
public class ApiController {

    private final PetitionService petitionService;

    public ApiController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<PetitionDTO>>> getAllPetitions(
            @RequestParam(required = false) PetitionStatus status) {
        List<Petition> petitions;
        if (status != null) {
            petitions = petitionService.findByStatus(status);
        } else {
            petitions = petitionService.findAll();
        }

        List<PetitionDTO> dtos = petitions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("petitions", dtos));
    }

    private PetitionDTO convertToDTO(Petition petition) {
        return PetitionDTO.builder()
                .petition_id(petition.getId().toString())
                .status(petition.getStatus().toString().toLowerCase())
                .petition_title(petition.getTitle())
                .petition_text(petition.getContent())
                .petitioner(petition.getCreator().getEmail())
                .signatures(String.valueOf(petition.getSignatures().size()))
                .response(petition.getResponse())
                .build();
    }
}
