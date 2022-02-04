package com.openclassrooms.payMyBuddy.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String sender;
    private float amount;
    private String receiver;
    private String date;
    private String time;
    private String description;

}
