package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.configuration.SpringSecurityConfiguration;
import com.openclassrooms.payMyBuddy.exceptions.*;
import com.openclassrooms.payMyBuddy.model.*;
import com.openclassrooms.payMyBuddy.model.DTO.*;
import com.openclassrooms.payMyBuddy.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

    @Override
    public List<PersonDTO> getAllPersonsDTO() {
        log.debug("The function getAllPersonsDTO in PersonService is beginning.");
        List<PersonDTO> allPersonDTOList = getAllPersons().stream().map(this::transformPersonToPersonDTO).collect(Collectors.toList());
        log.debug("The function getAllPersonsDTO in PersonService is ending without exception.");
        return allPersonDTOList;
    }

    private List<Person> getAllPersons() throws EmptyObjectException {
        log.debug("The function getAllPersons in PersonService is beginning.");
        List<Person> allPersonList = (List<Person>) personRepository.findAll();
        if (allPersonList.isEmpty()) {
            throw new EmptyObjectException("There is not any person registered.\n");
        }
        log.debug("The function getAllPersons in PersonService is ending without exception.");
        return allPersonList;
    }

    @Override
    public List<PersonDTO> getAllActivePersonsDTO() {
        log.debug("The function getAllActivePersonsDTO in PersonService is beginning.");
        List<PersonDTO> allPersonDTOList = getAllActivePersons().stream().map(this::transformPersonToPersonDTO).collect(Collectors.toList());
        log.debug("The function getAllActivePersonsDTO in PersonService is ending without exception.");
        return allPersonDTOList;
    }

    private List<Person> getAllActivePersons() {
        log.debug("The function getAllActivePersons in PersonService is beginning.");
        List<Person> allPersonList = personRepository.findByActive(true);
        log.debug("The function getAllActivePersons in PersonService is ending without exception.");
        return allPersonList;
    }

    @Override
    public List<Person> getAllInactivePersons() {
        log.debug("The function getAllInactivePersons in PersonService is beginning.");
        List<Person> allPersonList = personRepository.findByActive(false);
        log.debug("The function getAllInactivePersons in PersonService is ending without exception.");
        return allPersonList;
    }



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

    @Override
    public PersonDTO getPersonDTO(String mail) {
        log.debug("The function getPersonDTO in PersonService is beginning.");
        Person person = getPerson(mail);
        PersonDTO personDTO = transformPersonToPersonDTO(person);
        log.debug("The function getPersonDTO in PersonService is ending without exception.");
        return personDTO;
    }

    @Override
    public String getCurrentUserMail() throws NotFoundObjectException {
        log.debug("The function getCurrentUserMail in PersonService is beginning.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (!authentication.isAuthenticated()) {
                throw new NotFoundObjectException("The current user's mail couldn't have been found.\n");
            } else {
                String currentUserMail = authentication.getName();
                log.debug("The function getCurrentUserMail in PersonService is ending without exception.");
                return currentUserMail;
            }
        } else {
            throw new NotFoundObjectException("The current user's mail couldn't have been found.\n");
        }
    }

    @Override
    public String createPerson(PersonDTO personDTO) {
        log.debug("The function createPerson in PersonService is beginning.");
        Person person = new Person(personDTO.getEmail(), "", personDTO.getFirstName(), personDTO.getLastName());
        person.setPassword(springSecurityConfiguration.passwordEncoder().encode(personDTO.getPassword()));
        person.setRole(Role.USER);
        person = personRepository.save(person);
        String message = "The person " + person.getFirstName() + " " + person.getLastName() + " have been created.\n";
        log.info(message);
        log.debug("The function createPerson in PersonService is ending without exception.");
        return message;
    }

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

    @Override
    public String deletePerson(String email) {
        log.debug("The function deletePerson in PersonService is beginning.");
        Person person;
        try {
            person = getPerson(email);
        } catch (NotFoundObjectException | ObjectNotExistingAnymoreException exception) {
            throw new NothingToDoException("The person " + email + " was not found, so it couldn't have been deleted");
        }
        person.getRelations().clear();
        person.getBankAccountList().clear();
        person.setActive(false);
        personRepository.save(person);
        String message = "The person " + email + " have been deleted.\n";
        log.info(message);
        log.debug("The function deletePerson in PersonService is ending without exception.");
        return message;
    }

    @Override
    public String addPersonInGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO newPersonInGroupDTO) throws ObjectNotExistingAnymoreException, ObjectAlreadyExistingException {
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

    @Override
    public String removePersonFromGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO personRemovedFromGroup) throws ObjectNotExistingAnymoreException, ObjectAlreadyExistingException {
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

    @Override
    public String reactivateAccount (String email){
        log.debug("The function reactivateAccount in PersonService is beginning.");
        Optional<Person> personOptional = personRepository.findById(email);
        if(personOptional.isPresent()){
            Person person = personOptional.get();
            person.setActive(true);
            personRepository.save(person);
            String message = "The person whose mail is " + email + " has been reactivated.\n";
            log.debug("The function reactivateAccount in PersonService is beginning.");
            return message;
        }else{
            throw new NotFoundObjectException("The person " + email + " was not found");}
    }

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


    public List<BankAccount> getPersonBankAccounts(PersonDTO personDTO) {
        log.debug("The function getPersonBankAccounts in PersonService is beginning.");
        Person person = getPerson(personDTO.getEmail());
        List<BankAccount> bankAccountList = person.getBankAccountList().stream().filter(BankAccount::isActiveBankAccount).collect(Collectors.toList());
        log.debug("The function getPersonBankAccounts in PersonService is ending without any exception.");
        return bankAccountList;
    }

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

    @Override
    public List<TransactionDTO> getPersonTransactionsReceived(PersonDTO personDTO) {
        log.debug("The function getTransactionsReceived in PersonService is beginning.");
        Person person = getPerson(personDTO.getEmail());
        List<TransactionDTO> transactionsReceived = person.getTransactionReceivedList().stream().map(transaction -> transactionService.transformTransactionToTransactionDTO(transaction)).collect(Collectors.toList());
        log.debug("The function getTransactionsReceived in PersonService is ending without any exception.");
        return transactionsReceived;
    }

    @Override
    public boolean emailAlreadyExists(String email){
        return personRepository.existsById(email);
    }

    @Override
    public ListTransactionPagesDTO displayTransactionsByPage(PersonDTO personDTO, int pageNumber, int numberOfTransactionByPage, String transactionType) {
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
