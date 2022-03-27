package com.openclassrooms.payMyBuddy.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonDTO {

    @NotBlank(message = "You have to enter an email")
    @Email(message = "Your email is not valid")
    private String email;
    @NotBlank(message = "You have to enter a password")
    private String password;
    @NotBlank(message = "You have to enter your first name")
    private String firstName;
    @NotBlank(message = "You have to enter your last name")
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
