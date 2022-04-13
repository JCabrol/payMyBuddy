package com.openclassrooms.paymybuddy.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListMessagesDTO {

    private Integer totalNumberOfPage;
    private Integer currentPage;
    private Integer[] pagesToDisplay;
    private List<MessageDTO> messagesToDisplay;

}
