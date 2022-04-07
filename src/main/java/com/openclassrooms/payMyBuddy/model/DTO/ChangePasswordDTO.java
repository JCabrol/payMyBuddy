package com.openclassrooms.payMyBuddy.model.DTO;

import com.openclassrooms.payMyBuddy.validation.ValidPassword;
import com.openclassrooms.payMyBuddy.validation.ValidPasswordConfirmation;
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
