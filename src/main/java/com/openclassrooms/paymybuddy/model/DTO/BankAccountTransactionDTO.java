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
public class BankAccountTransactionDTO {
    @NotBlank(message = "Please select the bank account you want to remove")
    private String iban;
    private String bic;
    private String usualName;

}
