package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.*;
import com.openclassrooms.payMyBuddy.model.DTO.ListTransactionPagesDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Person;
import com.openclassrooms.payMyBuddy.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public List<PersonDTO> getAllPersonsDTO() {
        log.debug("The function getAllPersonsDTO in PersonService is beginning.");
        List<PersonDTO> allPersonDTOList = getAllPersons().stream().map(this::transformPersonToPersonDTO).collect(Collectors.toList());
        log.debug("The function getAllPersonsDTO in PersonService is ending without exception.");
        return allPersonDTOList;
    }

    private List<Person> getAllPersons() {
        log.debug("The function getAllPersons in PersonService is beginning.");
        List<Person> allPersonList = (List<Person>) personRepository.findAll();
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
            throw new NotFoundObjectException("The person whose mail is " + mail + " was not found.");
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
    public String createPerson(PersonDTO personDTO) {
        log.debug("The function createPerson in PersonService is beginning.");
        Person person = new Person(personDTO.getEmail(), personDTO.getPassword(), personDTO.getFirstName(), personDTO.getLastName());
        person = personRepository.save(person);
        String message = "The person " + person.getFirstName() + " " + person.getLastName() + "have been created.\n";
        log.info(message);
        log.debug("The function createPerson in PersonService is ending without exception.");
        return message;
    }

    @Override
    public String deletePerson(PersonDTO personDTO) {
        log.debug("The function deletePerson in PersonService is beginning.");
        Person person;
        try {
            person = getPerson(personDTO.getEmail());
        } catch (NotFoundObjectException exception) {
            throw new NothingToDoException("The person " + personDTO.getFirstName() + " " + personDTO.getLastName() + " was not found, so it couldn't have been deleted");
        }
        if (person.isActive()) {
            person.getRelations().clear();
            person.getBankAccountList().clear();
            person.setActive(false);
            String message = "Your account on PayMyBuddy application have been deleted.\n";
            log.info("The person " + personDTO.getEmail() + " have been deleted.");
            log.debug("The function deletePerson in PersonService is ending without exception.");
            return message;
        } else {
            throw new NothingToDoException("The person " + personDTO.getFirstName() + " " + personDTO.getLastName() + " doesn't exist anymore in the application, so it couldn't have been deleted");
        }
    }

    @Override
    public String addPersonInGroup(PersonDTO groupOwnerDTO, PersonDTO newPersonInGroupDTO) throws ObjectNotExistingAnymoreException, ObjectAlreadyExistingException {
        log.debug("The function addPersonInGroup in PersonService is beginning.");
        Person groupOwner = getPerson(groupOwnerDTO.getEmail());
        Person newPersonInGroup = getPerson(newPersonInGroupDTO.getEmail());
        if (groupOwner.isActive()) {
            if (newPersonInGroup.isActive()) {
                if (groupOwner.getRelations().contains(newPersonInGroup)) {
                    throw new ObjectAlreadyExistingException("The person " + newPersonInGroup.getFirstName() + " " + newPersonInGroup.getLastName() + " is already present in your relations.\n");
                } else {
                    groupOwner.addPersonInGroup(newPersonInGroup);
                    personRepository.save(groupOwner);
                }
            } else {
                throw new ObjectNotExistingAnymoreException("The person " + newPersonInGroup.getFirstName() + " " + newPersonInGroup.getLastName() + " doesn't exist anymore in the application.\n");
            }
        } else {
            throw new ObjectNotExistingAnymoreException("The person " + groupOwner.getFirstName() + " " + groupOwner.getLastName() + " doesn't exist anymore in the application.\n");
        }
        String result = "The person " + newPersonInGroup.getFirstName() + " " + newPersonInGroup.getLastName() + " has been added in your relations.\n";
        log.info("The person " + newPersonInGroup.getEmail() + " has been added in " + groupOwner.getEmail() + "'s relations.");
        log.debug("The function addPersonInGroup in PersonService is ending without exception.");
        return result;
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

    //methode to help app creation
    //this method MUST BE DELETED when app finish
    @Override
    public void addMoney(PersonDTO personDTO, float amount) {
        Person person = getPerson(personDTO.getEmail());
        float balance = person.getAvailableBalance();
        person.setAvailableBalance(balance + amount);
        personRepository.save(person);
    }


    private PersonDTO transformPersonToPersonDTO(Person person) {
        log.debug("The function transformPersonToPersonDTO in PersonService is beginning.");
        PersonDTO personDTO = new PersonDTO(person.getEmail(), person.getFirstName(), person.getLastName());
        personDTO.setAvailableBalance(person.getAvailableBalance());
        if (person.getRelations().isEmpty()) {
            personDTO.setGroup(new ArrayList<>());
        } else {
            personDTO.setGroup(person.getRelations()
                    .stream()
                    .map(person1 -> new PersonDTO(person1.getEmail(), person1.getFirstName(), person1.getLastName()))
                    .collect(Collectors.toList()));
        }
        if (person.getTransactionMadeList().isEmpty()) {
            personDTO.setTransactionMadeList(new ArrayList<>());
        } else {
            personDTO.setTransactionMadeList(person.getTransactionMadeList()
                    .stream()
                    .map(transaction -> transactionService.transformTransactionToTransactionDTO(transaction))
                    .collect(Collectors.toList()));
        }
        if (person.getTransactionReceivedList().isEmpty()) {
            personDTO.setTransactionReceivedList(new ArrayList<>());
        } else {
            personDTO.setTransactionReceivedList(person.getTransactionReceivedList()
                    .stream()
                    .map(transaction -> transactionService.transformTransactionToTransactionDTO(transaction))
                    .collect(Collectors.toList()));
        }
        log.debug("The function transformPersonToPersonDTO in PersonService is ending without exception.");
        return personDTO;
    }

    @Override
    public List<TransactionDTO> getPersonTransactionsMade(PersonDTO personDTO) {
        log.debug("The function getTransactionsMade in PersonService is beginning.");
        Person person = getPerson(personDTO.getEmail());
        List<TransactionDTO> transactionsMade = person.getTransactionMadeList().stream().map(transaction -> transactionService.transformTransactionToTransactionDTO(transaction)).collect(Collectors.toList());
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
        int numberOfTransactions = allTransactions.size();
        int totalNumberOfPage = numberOfTransactions / numberOfTransactionByPage;
        int rest = numberOfTransactions % numberOfTransactionByPage;
        if (rest != 0) {
            totalNumberOfPage++;
        }
        int lastTransactionToDisplay = pageNumber * numberOfTransactionByPage;
        int firstTransactionToDisplay = lastTransactionToDisplay - numberOfTransactionByPage + 1;
        List<TransactionDTO> transactionsToDisplay = allTransactions.subList(firstTransactionToDisplay, lastTransactionToDisplay);
        ListTransactionPagesDTO result = new ListTransactionPagesDTO(totalNumberOfPage, pageNumber, transactionsToDisplay);
        log.debug("The function displayTransactionsByPage in PersonService is ending without any exception.");
        return result;
    }

}
