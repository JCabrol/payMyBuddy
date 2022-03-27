package com.openclassrooms.payMyBuddy.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    @NotBlank(message = "Please indicate your bank account's iban number")
    private String iban;
    @NotBlank(message = "Please indicate your bank account's bic number")
    private String bic;
    private String usualName;

    public BankAccountDTO(String iban,String bic){
        this.iban=iban;
        this.bic=bic;
    }
}
