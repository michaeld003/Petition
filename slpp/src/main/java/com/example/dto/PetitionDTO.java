package com.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetitionDTO {
    private String petition_id;
    private String status;
    private String petition_title;
    private String petition_text;
    private String petitioner;
    private String signatures;
    private String response;
}

