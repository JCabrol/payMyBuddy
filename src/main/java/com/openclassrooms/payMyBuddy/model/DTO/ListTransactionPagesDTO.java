package com.openclassrooms.payMyBuddy.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListTransactionPagesDTO {

    private int totalNumberOfPage;
    private int currentPage;
    private List<TransactionDTO> transactionsToDisplay;

}
