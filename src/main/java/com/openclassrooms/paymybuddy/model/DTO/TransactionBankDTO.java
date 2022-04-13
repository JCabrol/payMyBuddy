package com.openclassrooms.paymybuddy.model.DTO;

import com.openclassrooms.paymybuddy.validation.ValidAmount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidAmount
public class TransactionBankDTO {

    private String sender;
    @NotNull(message = "Enter an amount to make a transaction")
    private Float amount;
    @NotBlank(message = "Select a bank account to make a transaction")
    private String receiver;
}
