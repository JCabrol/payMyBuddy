package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.EmptyObjectException;
import com.openclassrooms.payMyBuddy.model.DTO.ListMessagesDTO;
import com.openclassrooms.payMyBuddy.model.DTO.MessageDTO;
import com.openclassrooms.payMyBuddy.model.MessageToAdmin;

import java.util.List;

public interface MessageToAdminService {

    String sendMessage(MessageDTO messageDTO);

    void readMessage(MessageToAdmin message);

    MessageDTO transformToDTO(MessageToAdmin message);

    MessageToAdmin getMessage(int id);

    List<MessageToAdmin> getAllMessages() throws EmptyObjectException;

    ListMessagesDTO displayMessagesByPage(int pageNumber, int numberOfTransactionByPage);
}
