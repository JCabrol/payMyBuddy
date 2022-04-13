package com.openclassrooms.paymybuddy.model.DTO;

import com.openclassrooms.paymybuddy.validation.ValidPassword;
import com.openclassrooms.paymybuddy.validation.ValidPasswordConfirmation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidPassword
@ValidPasswordConfirmation
public class ChangePasswordDTO {

    @NotBlank(message = "Please enter a new password to change it.")
    private String password;
    @NotBlank(message = "Please confirm your new password.")
    private String confirmationPassword;
}
