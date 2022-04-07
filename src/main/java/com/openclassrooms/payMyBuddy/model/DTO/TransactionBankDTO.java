package com.openclassrooms.payMyBuddy.model.DTO;

import com.openclassrooms.payMyBuddy.validation.ValidAmount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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
//    private String date;
//    private String time;
//    @Size(max = 80,message="The description should be 80 characters maximum")
//    private String description;
}
