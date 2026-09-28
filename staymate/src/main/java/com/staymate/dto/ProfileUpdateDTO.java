package com.staymate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProfileUpdateDTO(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Phone is required")
        @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Enter a valid 10-digit phone number"
        )
        String phone

) {

}
