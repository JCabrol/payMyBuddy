package com.openclassrooms.payMyBuddy.model.DTO;

import com.openclassrooms.payMyBuddy.validation.ValidIban;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidIban
public class BankAccountDTO {
    @NotBlank(message = "Please indicate your bank account's iban number")
    private String iban;
    @NotBlank(message = "Please indicate your bank account's bic number")
    @Size(min = 8,max = 11, message = "Your BIC code is not valid, please read information above to learn more.")
    private String bic;
    private String usualName;

    public BankAccountDTO(String iban,String bic){
        this.iban=iban;
        this.bic=bic;
    }
}
