package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectAlreadyExistingException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.payMyBuddy.model.DTO.ListTransactionPagesDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Person;

import java.util.List;

public interface PersonService {

    List<PersonDTO> getAllPersonsDTO();

    List<PersonDTO> getAllActivePersonsDTO();

    Person getPerson(String mail) throws NotFoundObjectException, ObjectNotExistingAnymoreException;

    PersonDTO getPersonDTO(String mail);

    String createPerson(PersonDTO personDTO);

    String deletePerson(PersonDTO personDTO);

    String addPersonInGroup(PersonDTO groupOwnerDTO,PersonDTO newPersonInGroupDTO) throws ObjectAlreadyExistingException,ObjectNotExistingAnymoreException;

   String updatePerson(PersonDTO personDTO);

    List<TransactionDTO> getPersonTransactionsMade (PersonDTO personDTO);

    List<TransactionDTO> getPersonTransactionsReceived (PersonDTO personDTO);

    //methode to help app creation
    //this method MUST BE DELETED when app finish
    void addMoney(PersonDTO personDTO, float amount);

    ListTransactionPagesDTO displayTransactionsByPage(PersonDTO personDTO, int pageNumber, int numberOfTransactionByPage, String transactionType);
}
