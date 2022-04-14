package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.EmptyObjectException;
import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.model.DTO.*;
import com.openclassrooms.payMyBuddy.model.MessageToAdmin;
import com.openclassrooms.payMyBuddy.repository.MessageToAdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageToAdminServiceImpl implements MessageToAdminService {

    @Autowired
    private MessageToAdminRepository messageToAdminRepository;

    @Override
    public String sendMessage(MessageDTO messageDTO)
    {
        log.debug("The function sendMessage in MessageToAdminService is beginning.");
        MessageToAdmin message = new MessageToAdmin(messageDTO.getSubject(),messageDTO.getMessage(),messageDTO.getEmail(),messageDTO.getFirstName(),messageDTO.getLastName());
        messageToAdminRepository.save(message);
       String returnMessage = "You message \"" + message.getSubject() + "\" has been send to administrator.";
        log.debug("The function sendMessage in MessageToAdminService is ending without exception.");
       return returnMessage;
    }

    @Override
    public void readMessage(MessageToAdmin message)
    {
        log.debug("The function readMessage in MessageToAdminService is beginning.");
        message.setNewMessage(false);
        messageToAdminRepository.save(message);
        log.debug("The function readMessage in MessageToAdminService is ending without exception.");
    }

    @Override
    public MessageDTO transformToDTO(MessageToAdmin message)
    {
        log.debug("The function transformToDTO in MessageToAdminService is beginning.");
        MessageDTO messageDTO =new MessageDTO(message.getSubject(),message.getMessage(),message.getFirstName(),message.getLastName(),message.getEmail(),message.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),message.getNewMessage(),message.getMessageId());
        log.debug("The function transformToDTO in MessageToAdminService is ending without exception.");
        return messageDTO;
    }

    @Override
    public MessageToAdmin getMessage(int id)
    {
        log.debug("The function getMessage in MessageToAdminService is beginning.");
        Optional<MessageToAdmin> messageOptional = messageToAdminRepository.findById(id);
        if (messageOptional.isPresent()) {
            MessageToAdmin message = messageOptional.get();
        log.debug("The function getMessage in MessageToAdminService is ending without exception.");
        return message;
        } else {
            throw new NotFoundObjectException("The message number " + id + " was not found.\n");
        }
    }

    @Override
    public List<MessageToAdmin> getAllMessages() throws EmptyObjectException {
        log.debug("The function getAllMessages in MessageToAdminService is beginning.");
        List<MessageToAdmin> allMessagesList = (List<MessageToAdmin>) messageToAdminRepository.findAll();
        if (allMessagesList.isEmpty()) {
            throw new EmptyObjectException("There is not any message registered.\n");
        }
        log.debug("The function getAllMessages in MessageToAdminService is ending without exception.");
        return allMessagesList;
    }

    @Override
    public ListMessagesDTO displayMessagesByPage(int pageNumber, int numberOfMessagesByPage) {
        log.debug("The function displayMessagesByPage in MessageToAdminService is beginning.");
        List<MessageToAdmin> allMessages=getAllMessages();
        Collections.reverse(allMessages);
        int numberOfMessages = allMessages.size();
        int totalNumberOfPage = numberOfMessages / numberOfMessagesByPage;
        int rest = numberOfMessages % numberOfMessagesByPage;
        if (rest != 0) {
            totalNumberOfPage++;
        }
        int lastMessageToDisplay = pageNumber * numberOfMessagesByPage;
        int firstMessageToDisplay = lastMessageToDisplay - numberOfMessagesByPage;
        List<MessageToAdmin> messagesToAdminToDisplay;
        if (lastMessageToDisplay <= numberOfMessages) {
            messagesToAdminToDisplay = allMessages.subList(firstMessageToDisplay, lastMessageToDisplay);
        } else {
            messagesToAdminToDisplay = allMessages.subList(firstMessageToDisplay, numberOfMessages);
        }
List<MessageDTO> messagesToDisplay = messagesToAdminToDisplay
        .stream()
        .map(this::transformToDTO)
        .collect(Collectors.toList());
        Integer[] pagesToDisplay = new Integer[5];
        if (totalNumberOfPage < 5) {
            for (int i = 0; i < totalNumberOfPage; i++) {
                pagesToDisplay[i] = i + 1;
            }
        } else {
            if (pageNumber < 3) {
                for (int i = 0; i < 5; i++) {
                    pagesToDisplay[i] = i + 1;
                }
            } else {
                if (pageNumber > (totalNumberOfPage - 2)) {
                    for (int i = 0; i < 5; i++) {
                        pagesToDisplay[i] = totalNumberOfPage - 4 + i;
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        pagesToDisplay[i] = i + pageNumber - 2;
                    }
                }
            }
        }

        ListMessagesDTO result = new ListMessagesDTO(totalNumberOfPage, pageNumber, pagesToDisplay, messagesToDisplay);
        log.debug("The function displayMessagesByPage in MessageToAdminService is ending without any exception.");
        return result;
    }

}
