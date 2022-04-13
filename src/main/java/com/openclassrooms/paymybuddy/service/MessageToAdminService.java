package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.EmptyObjectException;
import com.openclassrooms.paymybuddy.model.DTO.ListMessagesDTO;
import com.openclassrooms.paymybuddy.model.DTO.MessageDTO;
import com.openclassrooms.paymybuddy.model.MessageToAdmin;

import java.util.List;

public interface MessageToAdminService {

    String sendMessage(MessageDTO messageDTO);

    void readMessage(MessageToAdmin message);

    MessageDTO transformToDTO(MessageToAdmin message);

    MessageToAdmin getMessage(int id);

    List<MessageToAdmin> getAllMessages() throws EmptyObjectException;

    ListMessagesDTO displayMessagesByPage(int pageNumber, int numberOfTransactionByPage);
}
