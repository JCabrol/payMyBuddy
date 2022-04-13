package com.openclassrooms.paymybuddy.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonConnectionDTO {

    @NotBlank(message = "Please select a person in the list.")
    private String email;
}
