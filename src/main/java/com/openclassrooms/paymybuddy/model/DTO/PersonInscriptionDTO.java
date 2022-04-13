package com.openclassrooms.paymybuddy.model.DTO;


import com.openclassrooms.paymybuddy.validation.ValidPassword;
import com.openclassrooms.paymybuddy.validation.ValidPasswordConfirmation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidPassword
@ValidPasswordConfirmation
public class PersonInscriptionDTO {

    @NotBlank(message = "You have to enter an email")
    @Email(message = "Your email is not valid")
    private String email;
    @NotBlank(message = "You have to enter a password")
    private String password;
    @NotBlank(message = "Please confirm your password")
    private String confirmationPassword;
    @NotBlank(message = "You have to enter your first name")
    private String firstName;
    @NotBlank(message = "You have to enter your last name")
    private String lastName;


}