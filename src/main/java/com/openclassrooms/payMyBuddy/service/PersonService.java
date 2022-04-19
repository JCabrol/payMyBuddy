package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.*;
import com.openclassrooms.payMyBuddy.model.DTO.*;
import com.openclassrooms.payMyBuddy.model.Person;

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

    /**
     * Add a person in another person's group to permit transactions
     *
     * @param groupOwnerDTO a PersonDTO object which is the group owner
     * @param newPersonInGroupDTO a PersonConnectionDTO object which is the new person to add in the group
     * @return a String which is a success message indicating the new person has been added in the group
     * @throws ObjectAlreadyExistingException when the person to add is already present in the group
     */
    String addPersonInGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO newPersonInGroupDTO) throws ObjectAlreadyExistingException;

    /**
     * Remove a person from another person's group to prohibit transactions
     *
     * @param groupOwnerDTO a PersonDTO object which is the group owner
     * @param personRemovedFromGroup a PersonConnectionDTO object which is the person to remove from the group
     * @return a String which is a success message indicating the person has been removed from the group
     * @throws NothingToDoException when the person to remove is not present in the group
     */
    String removePersonFromGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO personRemovedFromGroup) throws NothingToDoException;

    /**
     * Get all active personDTO object in the application which are not present in the list of the personDTO given in argument
     *
     * @param personDTO a PersonDTO object which is the group owner
     * @return a list of PersonDTO object which are all the active person not present in the person list
     */
    List<PersonDTO> getAllNotFriendPersonsDTO(PersonDTO personDTO);

    /**
     * Get all the transactions made by the person given in argument
     *
     * @param personDTO a PersonDTO object which is the person whose transactions have to be returned
     * @return a list of TransactionDTO object which are all the transactions made by the person given in argument
     */
    List<TransactionDTO> getPersonTransactionsMade(PersonDTO personDTO);

    /**
     * Get all the transactions received by the person given in argument
     *
     * @param personDTO a PersonDTO object which is the person whose transactions have to be returned
     * @return a list of TransactionDTO object which are all the transactions received by the person given in argument
     */
    List<TransactionDTO> getPersonTransactionsReceived(PersonDTO personDTO);

    /**
     * Add a bank account to a person
     *
     * @param personDTO a PersonDTO object which is the person to which a bank account has to be added
     * @param bankAccountDTO a BankAccountDTO object containing information given by user to create and add a bank account
     * @return a success message indicating the bank account has been created and added to the person list
     */
    String addBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO);

    /**
     * Remove a bank account from a person bank account list
     *
     * @param personDTO a PersonDTO object which is the person to which a bank account has to be removed
     * @param bankAccountDTO a BankAccountDTO object which is the bank account to remove
     * @return a success message indicating the bank account has been removed from the person list
     */
    String removeBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO);

    /**
     * Return all necessary information to display transactions by page
     *
     * @param personDTO a PersonDTO object which is the person whose transactions have to be displayed
     * @param pageNumber an int which is the number of the page to display
     * @param numberOfTransactionByPage which is the number of transactions to display on one page
     * @param transactionType a String which can be either "made" or "received" indicating the type of transaction to display
     * @return a ListTransactionPageDTO object containing all information to display the right transactions
     * @throws NotValidException when the transaction type is not correct ("made" or "received")
     */
    ListTransactionPagesDTO displayTransactionsByPage(PersonDTO personDTO, int pageNumber, int numberOfTransactionByPage, String transactionType);
}
