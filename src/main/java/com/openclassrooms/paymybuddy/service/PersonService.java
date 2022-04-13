package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.*;
import com.openclassrooms.paymybuddy.model.DTO.*;
import com.openclassrooms.paymybuddy.model.Person;

import java.util.List;

public interface PersonService {

    /**
     * Get all person registered, transformed in PersonDTO object
     *
     * @return a list of all the person registered transformed in PersonDTO object
     * @throws EmptyObjectException when there is no person registered
     */
    List<PersonDTO> getAllPersonsDTO() throws EmptyObjectException;

    /**
     * Get all active persons, transformed in PersonDTO object
     *
     * @return a list of all the Person objects which are active transformed in PersonDTO object, an empty list if there is not any active person
     */
    List<PersonDTO> getAllActivePersonsDTO();

    /**
     * Get all inactive persons
     *
     * @return a list of all the Person objects which are inactive, an empty list if there is not any inactive person
     */
    List<Person> getAllInactivePersons();

    /**
     * Get one person from his id
     *
     * @param mail a String which is the id of the researched person
     * @return the Person object having the researched mail as id
     * @throws NotFoundObjectException when the researched mail is not registered
     * @throws ObjectNotExistingAnymoreException when the researched mail correspond to an inactive person
     */
    Person getPerson(String mail) throws NotFoundObjectException, ObjectNotExistingAnymoreException;

    /**
     * Get one person transformed in PersonDTO object from his id
     *
     * @param mail a String which is the id of the researched person
     * @return the Person object having the researched mail as id transformed in PersonDTO object
     */
    PersonDTO getPersonDTO(String mail);

    /**
     * Get the mail (which is the id) of the connected user
     *
     * @return a String which is the connected user's mail
     * @throws NotFoundObjectException when there is no connected user
     */
    String getCurrentUserMail() throws NotFoundObjectException;

    /**
     * Create a new Person object using user's given information
     *
     * @param personDTO a PersonDTO object containing all information given by the user to create a new person
     * @return a String which is a success message indicating the person has been created
     * @throws MissingInformationException when there is not all required information
     */
    String createPerson(PersonDTO personDTO);

    /**
     * Check if an email is already registered
     *
     * @param email a String which is the researched email
     * @return true if the email already exists, false otherwise
     */
    boolean emailAlreadyExists(String email);

    /**
     * Update a Person object using user's given information
     *
     * @param personDTO a PersonDTO object containing information given by the user to update the person
     * @return a String which is a success message indicating the person has been updated
     */
    String updatePerson(PersonDTO personDTO);

    /**
     * Encode and update a person's password
     *
     * @param personDTO a PersonDTO object whose password has to be modified
     * @param password a String which is the new password
     * @return a String which is a success message indicating the password has been changed
     */
    String changePassword(PersonDTO personDTO, String password);

    /**
     * Delete a person from application (the person is not really suppressed but its status is set to inactive)
     *
     * @param email a String which is the id of the person to delete
     * @return a String which is a success message indicating the person has been deleted
     * @throws NothingToDoException when the person to delete doesn't exist or is already deleted
     */
    String deletePerson(String email) throws NothingToDoException;

    /**
     * Reactivate an account which has been suppressed
     *
     * @param email a String which is the id of the person to reactivate
     * @return a String which is a success message indicating the person has been reactivated
     * @throws NotFoundObjectException when the person to reactivate doesn't exist
     */
    String reactivateAccount(String email) throws NotFoundObjectException;

    String addPersonInGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO newPersonInGroupDTO) throws ObjectAlreadyExistingException, ObjectNotExistingAnymoreException;

    String removePersonFromGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO personRemovedFromGroup) throws ObjectNotExistingAnymoreException, ObjectAlreadyExistingException;

    List<PersonDTO> getAllNotFriendPersonsDTO(PersonDTO personDTO);

    List<TransactionDTO> getPersonTransactionsMade(PersonDTO personDTO);

    List<TransactionDTO> getPersonTransactionsReceived(PersonDTO personDTO);

    ListTransactionPagesDTO displayTransactionsByPage(PersonDTO personDTO, int pageNumber, int numberOfTransactionByPage, String transactionType);

    String addBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO);

    String removeBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO);

}
