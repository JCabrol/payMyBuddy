package com.openclassrooms.payMyBuddy.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonDTO {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String availableBalance;
    private List<PersonDTO> group;
    private List<TransactionDTO> transactionMadeList;
    private List<TransactionDTO> transactionReceivedList;
    private List<BankAccountDTO> bankAccountDTOList;

    public PersonDTO(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersonDTO(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
