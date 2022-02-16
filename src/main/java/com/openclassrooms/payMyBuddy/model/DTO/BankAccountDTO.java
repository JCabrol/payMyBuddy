package com.openclassrooms.payMyBuddy.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    private String iban;
    private String bic;
    private String usualName;

    public BankAccountDTO(String iban,String bic){
        this.iban=iban;
        this.bic=bic;
    }
}
