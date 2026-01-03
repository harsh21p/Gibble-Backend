package com.practicum.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionRequest {

    @NotBlank
    private String submissionLink;
}
