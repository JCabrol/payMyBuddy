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
public class UpdatePersonDTO {
    @NotBlank(message = "You have to enter your first name")
    private String firstName;
    @NotBlank(message = "You have to enter your last name")
    private String lastName;
}
