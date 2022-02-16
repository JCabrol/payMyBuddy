package com.openclassrooms.payMyBuddy.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListTransactionPagesDTO {

    private Integer totalNumberOfPage;
    private Integer currentPage;
    private Integer beforePreviousPage;
    private Integer previousPage;
    private Integer nextPage;
    private Integer afterNextPage;
    private List<TransactionDTO> transactionsToDisplay;
    private String transactionType;

}
