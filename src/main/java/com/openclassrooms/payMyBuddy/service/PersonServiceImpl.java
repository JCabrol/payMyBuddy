package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.configuration.SpringSecurityConfiguration;
import com.openclassrooms.payMyBuddy.exceptions.*;
import com.openclassrooms.payMyBuddy.model.BankAccount;
import com.openclassrooms.payMyBuddy.model.DTO.*;
import com.openclassrooms.payMyBuddy.model.Person;
import com.openclassrooms.payMyBuddy.model.Role;
import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private SpringSecurityConfiguration springSecurityConfiguration;

    /**
     * Get all person registered
     *
     * @return a list of all the Person objects which are registered
     * @throws EmptyObjectException when there is no person registered
     */
    private List<Person> getAllPersons() throws EmptyObjectException {
        log.debug("The function getAllPersons in PersonService is beginning.");
        List<Person> allPersonList = (List<Person>) personRepository.findAll();
        if (allPersonList.isEmpty()) {
            throw new EmptyObjectException("There is not any person registered.\n");
        }
        log.debug("The function getAllPersons in PersonService is ending without exception.");
        return allPersonList;
    }

    /**
     * Get all person registered, transformed in PersonDTO object
     *
     * @return a list of all the person registered transformed in PersonDTO object
     * @throws EmptyObjectException when there is no person registered
     */
    @Override
    public List<PersonDTO> getAllPersonsDTO() throws EmptyObjectException {
        log.debug("The function getAllPersonsDTO in PersonService is beginning.");
        List<PersonDTO> allPersonDTOList = getAllPersons().stream().map(this::transformPersonToPersonDTO).collect(Collectors.toList());
        log.debug("The function getAllPersonsDTO in PersonService is ending without exception.");
        return allPersonDTOList;
    }

    /**
     * Get all active persons
     *
     * @return a list of all the Person objects which are active, an empty list if there is not any active person
     */
    private List<Person> getAllActivePersons() {
        log.debug("The function getAllActivePersons in PersonService is beginning.");
        List<Person> allPersonList = personRepository.findByActive(true);
        log.debug("The function getAllActivePersons in PersonService is ending without exception.");
        return allPersonList;
    }

    /**
     * Get all active persons, transformed in PersonDTO object
     *
     * @return a list of all the Person objects which are active transformed in PersonDTO object, an empty list if there is not any active person
     */
    @Override
    public List<PersonDTO> getAllActivePersonsDTO() {
        log.debug("The function getAllActivePersonsDTO in PersonService is beginning.");
        List<PersonDTO> allPersonDTOList = getAllActivePersons().stream().map(this::transformPersonToPersonDTO).collect(Collectors.toList());
        log.debug("The function getAllActivePersonsDTO in PersonService is ending without exception.");
        return allPersonDTOList;
    }

    /**
     * Get all inactive persons
     *
     * @return a list of all the Person objects which are inactive, an empty list if there is not any inactive person
     */
    @Override
    public List<Person> getAllInactivePersons() {
        log.debug("The function getAllInactivePersons in PersonService is beginning.");
        List<Person> allPersonList = personRepository.findByActive(false);
        log.debug("The function getAllInactivePersons in PersonService is ending without exception.");
        return allPersonList;
    }

    /**
     * Get one person from his id
     *
     * @param mail a String which is the id of the researched person
     * @return the Person object having the researched mail as id
     * @throws NotFoundObjectException           when the researched mail is not registered
     * @throws ObjectNotExistingAnymoreException when the researched mail correspond to an inactive person
     */
    @Override
    public Person getPerson(String mail) throws NotFoundObjectException, ObjectNotExistingAnymoreException {
        log.debug("The function getPerson in PersonService is beginning.");
        Optional<Person> personOptional = personRepository.findById(mail);
        if (personOptional.isPresent()) {
            Person result = personOptional.get();
            if (result.isActive()) {
                log.debug("The function getPerson in PersonService is ending without exception.");
                return result;
            } else {
                throw new ObjectNotExistingAnymoreException("The person whose mail is " + mail + " doesn't exist anymore in the application.\n");
            }
        } else {
            throw new NotFoundObjectException("The person whose mail is " + mail + " was not found.\n");
        }
    }

    /**
     * Get one person transformed in PersonDTO object from his id
     *
     * @param mail a String which is the id of the researched person
     * @return the Person object having the researched mail as id transformed in PersonDTO object
     */
    @Override
    public PersonDTO getPersonDTO(String mail) {
        log.debug("The function getPersonDTO in PersonService is beginning.");
        Person person = getPerson(mail);
        PersonDTO personDTO = transformPersonToPersonDTO(person);
        log.debug("The function getPersonDTO in PersonService is ending without exception.");
        return personDTO;
    }

    /**
     * Get the mail (which is the id) of the connected user
     *
     * @return a String which is the connected user's mail
     * @throws NotFoundObjectException when there is no connected user
     */
    @Override
    public String getCurrentUserMail() throws NotFoundObjectException {
        log.debug("The function getCurrentUserMail in PersonService is beginning.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new NotFoundObjectException("The current user's mail couldn't have been found.\n");
        } else {
            String currentUserMail = authentication.getName();
            log.debug("The function getCurrentUserMail in PersonService is ending without exception.");
            return currentUserMail;
        }
    }

    /**
     * Create a new Person object using user's given information
     *
     * @param personDTO a PersonDTO object containing all information given by the user to create a new person
     * @return a String which is a success message indicating the person has been created
     * @throws MissingInformationException when there is not all required information
     */
    @Override
    public String createPerson(PersonDTO personDTO) throws MissingInformationException {
        log.debug("The function createPerson in PersonService is beginning.");
        if (personDTO.getEmail() != null && personDTO.getPassword() != null && personDTO.getFirstName() != null && personDTO.getLastName() != null) {
            Person person = new Person(personDTO.getEmail(), "", personDTO.getFirstName(), personDTO.getLastName());
            person.setPassword(springSecurityConfiguration.passwordEncoder().encode(personDTO.getPassword()));
            person.setRole(Role.USER);
            person = personRepository.save(person);
            String message = "The person " + person.getFirstName() + " " + person.getLastName() + " have been created.\n";
            log.info(message);
            log.debug("The function createPerson in PersonService is ending without exception.");
            return message;
        } else {
            throw new MissingInformationException("There is not all required information to create a person,\n" +
                    "email, password, first name and last name are mandatory.\n");
        }
    }

    /**
     * Check if an email is already registered
     *
     * @param email a String which is the researched email
     * @return true if the email already exists, false otherwise
     */
    @Override
    public boolean emailAlreadyExists(String email) {
        return personRepository.existsById(email);
    }

    /**
     * Update a Person object using user's given information
     *
     * @param personDTO a PersonDTO object containing information given by the user to update the person
     * @return a String which is a success message indicating the person has been updated
     */
    @Override
    public String updatePerson(PersonDTO personDTO) {
        log.debug("The function updatePerson in PersonService is beginning.");
        Person person = getPerson(personDTO.getEmail());

        if (personDTO.getFirstName() != null) {
            person.setFirstName(personDTO.getFirstName());
        }
        if (personDTO.getLastName() != null) {
            person.setLastName(personDTO.getLastName());
        }
        if (personDTO.getPassword() != null) {
            person.setPassword(personDTO.getPassword());
        }
        personRepository.save(person);
        String message = "Your user information has been updated.\n";
        log.info("User information about " + personDTO.getEmail() + " have been updated.");
        log.debug("The function updatePerson in PersonService is ending without exception.");
        return message;
    }

    /**
     * Transform a Person object to a PersonDTO object
     *
     * @param person a Person object which has to be transformed into PersonDTO object
     * @return a PersonDTO object containing all information which can be shown about the person given in argument
     */
    @Transactional
    private PersonDTO transformPersonToPersonDTO(Person person) {
        log.debug("The function transformPersonToPersonDTO in PersonService is beginning.");
        PersonDTO personDTO = new PersonDTO(person.getEmail(), person.getFirstName(), person.getLastName());
        DecimalFormat df = new DecimalFormat("#.##");
        personDTO.setAvailableBalance(df.format(person.getAvailableBalance()));
        if (person.getRelations().equals(Collections.emptyList())) {
            personDTO.setGroup(new ArrayList<>());
        } else {
            personDTO.setGroup(person.getRelations()
                    .stream()
                    .map(person1 -> new PersonDTO(person1.getEmail(), person1.getFirstName(), person1.getLastName()))
                    .collect(Collectors.toList()));
        }
        if (person.getTransactionMadeList().equals(Collections.emptyList())) {
            personDTO.setTransactionMadeList(new ArrayList<>());
        } else {
            personDTO.setTransactionMadeList(person.getTransactionMadeList()
                    .stream()
                    .map(transaction -> transactionService.transformTransactionToTransactionDTO(transaction))
                    .collect(Collectors.toList()));
        }
        if (person.getTransactionReceivedList().equals(Collections.emptyList())) {
            personDTO.setTransactionReceivedList(new ArrayList<>());
        } else {
            personDTO.setTransactionReceivedList(person.getTransactionReceivedList()
                    .stream()
                    .map(transaction -> transactionService.transformTransactionToTransactionDTO(transaction))
                    .collect(Collectors.toList()));
        }
        if (person.getBankAccountList().equals(Collections.emptyList())) {
            personDTO.setBankAccountDTOList(new ArrayList<>());
        } else {
            personDTO.setBankAccountDTOList(getPersonBankAccounts(personDTO)
                    .stream()
                    .map(bankAccount -> bankAccountService.transformBankAccountToBankAccountDTO(bankAccount))
                    .collect(Collectors.toList()));
        }
        log.debug("The function transformPersonToPersonDTO in PersonService is ending without exception.");
        return personDTO;
    }

    /**
     * Get all the active bank account owned by the person given in argument
     *
     * @param personDTO a PersonDTO object whose bank accounts has to be returned
     * @return list of BankAccounts
     */
    public List<BankAccount> getPersonBankAccounts(PersonDTO personDTO) {
        log.debug("The function getPersonBankAccounts in PersonService is beginning.");
        Person person = getPerson(personDTO.getEmail());
        List<BankAccount> bankAccountList = person.getBankAccountList().stream().filter(BankAccount::isActiveBankAccount).collect(Collectors.toList());
        log.debug("The function getPersonBankAccounts in PersonService is ending without any exception.");
        return bankAccountList;
    }

    /**
     * Encode and update a person's password
     *
     * @param personDTO a PersonDTO object whose password has to be modified
     * @param password  a String which is the new password
     * @return a String which is a success message indicating the password has been changed
     */
    @Override
    public String changePassword(PersonDTO personDTO, String password) {
        log.debug("The function changePassword in PersonService is beginning.");
        personDTO.setPassword(springSecurityConfiguration.passwordEncoder().encode(password));
        updatePerson(personDTO);
        String message = "Your password have been successfully modified.\n";
        log.info("The " + personDTO.getFirstName() + " " + personDTO.getLastName() + "'s password have been modified.\n");
        log.debug("The function changePassword in PersonService is ending without exception.");
        return message;
    }

    /**
     * Delete a person from application (the person is not really suppressed but its status is set to inactive)
     *
     * @param email a String which is the id of the person to delete
     * @return a String which is a success message indicating the person has been deleted
     * @throws NothingToDoException when the person to delete doesn't exist or is already deleted
     */
    @Override
    public String deletePerson(String email) throws NothingToDoException {
        log.debug("The function deletePerson in PersonService is beginning.");
        Person person;
        try {
            person = getPerson(email);
        } catch (NotFoundObjectException | ObjectNotExistingAnymoreException exception) {
            throw new NothingToDoException("The person " + email + " was not found, so it couldn't have been deleted");
        }
        person.getRelations().clear();
        person.getBankAccountList().clear();
        getAllPersons().forEach(person1 -> {
            if (person1.getRelations().contains(person)) {
                person1.removePersonInGroup(person);
                personRepository.save(person1);
            }
        });
        person.setActive(false);
        personRepository.save(person);
        String message = "The person " + email + " have been deleted.\n";
        log.info(message);
        log.debug("The function deletePerson in PersonService is ending without exception.");
        return message;
    }

    /**
     * Reactivate an account which has been suppressed
     *
     * @param email a String which is the id of the person to reactivate
     * @return a String which is a success message indicating the person has been reactivated
     * @throws NotFoundObjectException when the person to reactivate doesn't exist
     */
    @Override
    public String reactivateAccount(String email) throws NotFoundObjectException {
        log.debug("The function reactivateAccount in PersonService is beginning.");
        Optional<Person> personOptional = personRepository.findById(email);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            person.setActive(true);
            personRepository.save(person);
            String message = "The person whose mail is " + email + " has been reactivated.\n";
            log.debug("The function reactivateAccount in PersonService is beginning.");
            return message;
        } else {
            throw new NotFoundObjectException("The person " + email + " was not found");
        }
    }

    /**
     * Add a person in another person's group to permit transactions
     *
     * @param groupOwnerDTO a PersonDTO object which is the group owner
     * @param newPersonInGroupDTO a PersonConnectionDTO object which is the new person to add in the group
     * @return a String which is a success message indicating the new person has been added in the group
     * @throws ObjectAlreadyExistingException when the person to add is already present in the group
     */
    @Override
    public String addPersonInGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO newPersonInGroupDTO) throws ObjectAlreadyExistingException {
        log.debug("The function addPersonInGroup in PersonService is beginning.");
        Person groupOwner = getPerson(groupOwnerDTO.getEmail());
        Person newPersonInGroup = getPerson(newPersonInGroupDTO.getEmail());
        if (groupOwner.getRelations().contains(newPersonInGroup)) {
            throw new ObjectAlreadyExistingException("The person " + newPersonInGroup.getFirstName() + " " + newPersonInGroup.getLastName() + " is already present in your relations.\n");
        } else {
            groupOwner.addPersonInGroup(newPersonInGroup);
            personRepository.save(groupOwner);
        }
        String result = "The person " + newPersonInGroup.getFirstName() + " " + newPersonInGroup.getLastName() + " has been added in your relations.\n";
        log.info("The person " + newPersonInGroup.getEmail() + " has been added in " + groupOwner.getEmail() + "'s relations.");
        log.debug("The function addPersonInGroup in PersonService is ending without exception.");
        return result;
    }

    /**
     * Remove a person from another person's group to prohibit transactions
     *
     * @param groupOwnerDTO a PersonDTO object which is the group owner
     * @param personRemovedFromGroup a PersonConnectionDTO object which is the person to remove from the group
     * @return a String which is a success message indicating the person has been removed from the group
     * @throws NothingToDoException when the person to remove is not present in the group
     */
    @Override
    public String removePersonFromGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO personRemovedFromGroup) throws NothingToDoException {
        log.debug("The function addPersonInGroup in PersonService is beginning.");
        Person groupOwner = getPerson(groupOwnerDTO.getEmail());
        Person personRemoved = getPerson(personRemovedFromGroup.getEmail());
        if (!groupOwner.getRelations().contains(personRemoved)) {
            throw new NothingToDoException("The person " + personRemoved.getFirstName() + " " + personRemoved.getLastName() + " wasn't present in your relations.\n");
        } else {
            groupOwner.removePersonInGroup(personRemoved);
            personRepository.save(groupOwner);
        }
        String result = "The person " + personRemoved.getFirstName() + " " + personRemoved.getLastName() + " has been removed from your relations.\n";
        log.info("The person " + personRemoved.getEmail() + " has been removed from " + groupOwner.getEmail() + "'s relations.");
        log.debug("The function addPersonInGroup in PersonService is ending without exception.");
        return result;
    }

    /**
     * Get all active personDTO object in the application which are not present in the list of the personDTO given in argument
     *
     * @param personDTO a PersonDTO object which is the group owner
     * @return a list of PersonDTO object which are all the active person not present in the person list
     */
    @Override
    public List<PersonDTO> getAllNotFriendPersonsDTO(PersonDTO personDTO) {
        log.debug("The function getAllNotFriendPersonsDTO in PersonService is beginning.");
        List<String> allFriendsMails = personDTO.getGroup().stream().map(PersonDTO::getEmail).collect(Collectors.toList());
        List<PersonDTO> allActiveNotFriend = getAllActivePersonsDTO()
                .stream()
                .filter(person -> !person.getEmail().equals(personDTO.getEmail()))
                .filter(person -> !allFriendsMails.contains(person.getEmail()))
                .collect(Collectors.toList());
        log.debug("The function getAllNotFriendPersonsDTO in PersonService is ending without exception.");
        return allActiveNotFriend;
    }

    /**
     * Get all the transactions made by the person given in argument
     *
     * @param personDTO a PersonDTO object which is the person whose transactions have to be returned
     * @return a list of TransactionDTO object which are all the transactions made by the person given in argument
     */
    @Override
    public List<TransactionDTO> getPersonTransactionsMade(PersonDTO personDTO) {
        log.debug("The function getTransactionsMade in PersonService is beginning.");
        Person person = getPerson(personDTO.getEmail());
        List<Transaction> transactionsList = person.getTransactionMadeList();
        transactionsList.sort(Comparator.comparing(Transaction::getDateTime));
        List<TransactionDTO> transactionsMade = transactionsList.stream().map(transaction -> transactionService.transformTransactionToTransactionDTO(transaction)).collect(Collectors.toList());
        log.debug("The function getTransactionsMade in PersonService is ending without any exception.");
        return transactionsMade;
    }

    /**
     * Get all the transactions received by the person given in argument
     *
     * @param personDTO a PersonDTO object which is the person whose transactions have to be returned
     * @return a list of TransactionDTO object which are all the transactions received by the person given in argument
     */
    @Override
    public List<TransactionDTO> getPersonTransactionsReceived(PersonDTO personDTO) {
        log.debug("The function getTransactionsReceived in PersonService is beginning.");
        Person person = getPerson(personDTO.getEmail());
        List<TransactionDTO> transactionsReceived = person.getTransactionReceivedList().stream().map(transaction -> transactionService.transformTransactionToTransactionDTO(transaction)).collect(Collectors.toList());
        log.debug("The function getTransactionsReceived in PersonService is ending without any exception.");
        return transactionsReceived;
    }

    /**
     * Add a bank account to a person
     *
     * @param personDTO a PersonDTO object which is the person to which a bank account has to be added
     * @param bankAccountDTO a BankAccountDTO object containing information given by user to create and add a bank account
     * @return a success message indicating the bank account has been created and added to the person list
     */
    @Override
    public String addBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO) {
        log.debug("The function addBankAccount in PersonService is beginning.");
        String mail = personDTO.getEmail();
        String iban = bankAccountDTO.getIban();
        Person person = getPerson(mail);
        BankAccount bankAccount = bankAccountService.createBankAccount(bankAccountDTO);
        person.addBankAccount(bankAccount);
        personRepository.save(person);
        String message = "The bank account with IBAN number " + iban + " has been created and added to your bank account list.\n";
        log.info("The bank account with IBAN number " + iban + " has been created and added to " + mail + "'s bank account list.\n");
        log.debug("The function addBankAccount in PersonService is ending without exception.");
        return message;
    }

    /**
     * Remove a bank account from a person bank account list
     *
     * @param personDTO a PersonDTO object which is the person to which a bank account has to be removed
     * @param bankAccountDTO a BankAccountDTO object which is the bank account to remove
     * @return a success message indicating the bank account has been removed from the person list
     */
    @Override
    public String removeBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO) {
        log.debug("The function removeBankAccount in PersonService is beginning.");
        String email = personDTO.getEmail();
        String iban = bankAccountDTO.getIban();
        Person person = getPerson(email);
        BankAccount bankAccount = bankAccountService.getBankAccount(bankAccountDTO.getIban());
        person.removeBankAccount(bankAccount);
        personRepository.save(person);
        String message = "The bank account with IBAN number " + iban + " has been removed from your bank account list.\n";
        log.info("The bank account with IBAN number " + iban + " has been removed from " + email + "'s bank account list.\n");
        log.debug("The function removeBankAccount in PersonService is ending without exception.");
        return message;
    }

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
    @Override
    public ListTransactionPagesDTO displayTransactionsByPage(PersonDTO personDTO, int pageNumber, int numberOfTransactionByPage, String transactionType) throws NotValidException {
        log.debug("The function displayTransactionsByPage in PersonService is beginning.");
        List<TransactionDTO> allTransactions;
        if (transactionType.equals("made")) {
            allTransactions = getPersonTransactionsMade(personDTO);
        } else if (transactionType.equals("received")) {
            allTransactions = getPersonTransactionsReceived(personDTO);
        } else {
            throw new NotValidException("The transaction type must be made or received.\n");
        }
        Collections.reverse(allTransactions);
        int numberOfTransactions = allTransactions.size();
        int totalNumberOfPage = numberOfTransactions / numberOfTransactionByPage;
        int rest = numberOfTransactions % numberOfTransactionByPage;
        if (rest != 0) {
            totalNumberOfPage++;
        }
        int lastTransactionToDisplay = pageNumber * numberOfTransactionByPage;
        int firstTransactionToDisplay = lastTransactionToDisplay - numberOfTransactionByPage;
        List<TransactionDTO> transactionsToDisplay;
        if (lastTransactionToDisplay <= numberOfTransactions) {
            transactionsToDisplay = allTransactions.subList(firstTransactionToDisplay, lastTransactionToDisplay);
        } else {
            transactionsToDisplay = allTransactions.subList(firstTransactionToDisplay, numberOfTransactions);
        }

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

        ListTransactionPagesDTO result = new ListTransactionPagesDTO(totalNumberOfPage, pageNumber, pagesToDisplay, transactionsToDisplay, transactionType);
        log.debug("The function displayTransactionsByPage in PersonService is ending without any exception.");
        return result;
    }
}
