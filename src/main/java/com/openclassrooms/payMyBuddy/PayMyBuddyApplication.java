package com.openclassrooms.payMyBuddy;

import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.ListTransactionPagesDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.Person;
import com.openclassrooms.payMyBuddy.repository.PersonRepository;
import com.openclassrooms.payMyBuddy.repository.TransactionBetweenPersonsRepository;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class PayMyBuddyApplication implements CommandLineRunner {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private TransactionBetweenPersonsRepository transactionBetweenPersonsRepository;
    @Autowired
    private TransactionService transactionService;

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//
//        PersonDTO personDTO1 = new PersonDTO("person1@mail.fr", "password1", "firstName1", "lastName1");
//        personService.createPerson(personDTO1);
//        PersonDTO personDTO2 = new PersonDTO("person2@mail.fr", "password2", "firstName2", "lastName2");
//        personService.createPerson(personDTO2);
//        PersonDTO personDTO3 = new PersonDTO("person3@mail.fr", "password3", "firstName3", "lastName3");
//        personService.createPerson(personDTO3);
//        PersonDTO personDTO4 = new PersonDTO("person4@mail.fr", "password4", "firstName4", "lastName4");
//        personService.createPerson(personDTO4);
//        PersonDTO personDTO5 = new PersonDTO("person5@mail.fr", "password5", "firstName5", "lastName5");
//        personService.createPerson(personDTO5);
//        PersonDTO personDTO6 = new PersonDTO("person6@mail.fr", "password6", "firstName6", "lastName6");
//        personService.createPerson(personDTO6);
//        PersonDTO personDTO7 = new PersonDTO("person7@mail.fr", "password7", "firstName7", "lastName7");
//        personService.createPerson(personDTO7);
//        PersonDTO personDTO8 = new PersonDTO("person8@mail.fr", "password8", "firstName8", "lastName8");
//        personService.createPerson(personDTO8);

//Person personReal1 = personService.getPerson("person1@mail.fr");
//personReal1.setActive(true);
//personRepository.save(personReal1);

//        PersonDTO person1 = personService.getPersonDTO("person1@mail.fr");
//        PersonDTO person2 = personService.getPersonDTO("person2@mail.fr");
//        PersonDTO person3 = personService.getPersonDTO("person3@mail.fr");
//        PersonDTO person4 = personService.getPersonDTO("person4@mail.fr");
//        PersonDTO person5 = personService.getPersonDTO("person5@mail.fr");
//        PersonDTO person6 = personService.getPersonDTO("person6@mail.fr");
//        PersonDTO person7 = personService.getPersonDTO("person7@mail.fr");
//        PersonDTO person8 = personService.getPersonDTO("person8@mail.fr");

//personService.changePassword(person2, "password2");

//        personService.addBankAccount(person1,new BankAccountDTO("123456789123456789", "123456789", "compte courant-banque postale"));
//        personService.addBankAccount(person1, new BankAccountDTO("987654321987654321", "123456789", "compte joint-société générale"));
//        personService.addBankAccount(person2, new BankAccountDTO("456789123456789123", "123456789", "livret A-fortuneo banque"));
//        personService.addBankAccount(person3, new BankAccountDTO("123456789123456789123456789", "123456789", "compte courant-banque postale"));
//
//
//        personService.addPersonInGroup(person1, person3);
//        personService.addPersonInGroup(person1, person5);
//        personService.addPersonInGroup(person1, person2);
//        personService.addPersonInGroup(person1, person4);
//        personService.addPersonInGroup(person1, person7);
//        personService.addPersonInGroup(person1, person8);
//        personService.addPersonInGroup(person2, person6);
//        personService.addPersonInGroup(person3, person6);
//        personService.addPersonInGroup(person3, person7);
//        personService.addPersonInGroup(person3, person8);
//        personService.addPersonInGroup(person4, person7);
//
//
////        transactionService.makeANewTransactionWithBank(person1, 500, person1.getBankAccountDTOList().get(0));
////        transactionService.makeANewTransactionWithBank(person1, -100, person1.getBankAccountDTOList().get(1));
//
//
//        Person realPerson1 = personService.getPerson(person1.getEmail());
//        Person realPerson2 = personService.getPerson(person2.getEmail());
//        Person realPerson3 = personService.getPerson(person3.getEmail());
//        Person realPerson4 = personService.getPerson(person4.getEmail());
//        realPerson1.setAvailableBalance(200);
//        realPerson2.setAvailableBalance(500);
//        realPerson3.setAvailableBalance(1000);
//        realPerson4.setAvailableBalance(50);
//        personRepository.save(realPerson1);
//        personRepository.save(realPerson2);
//        personRepository.save(realPerson3);
//        personRepository.save(realPerson4);
//       person1 = personService.getPersonDTO("person1@mail.fr");
//        person2 = personService.getPersonDTO("person2@mail.fr");
//        person3 = personService.getPersonDTO("person3@mail.fr");
//        person4 = personService.getPersonDTO("person4@mail.fr");
//
//        transactionService.makeANewTransactionBetweenPersons(person1, 25, person3, "mum's's birthday");
//        transactionService.makeANewTransactionBetweenPersons(person1, 25, person5, "mum's birthday");
//        transactionService.makeANewTransactionBetweenPersons(person2, 100, person6, "restaurant");
//        transactionService.makeANewTransactionBetweenPersons(person3, 40, person6, "cinema");
//        transactionService.makeANewTransactionBetweenPersons(person3, 30, person7, "restaurant");
//        transactionService.makeANewTransactionBetweenPersons(person3, 50, person8, "");
//        transactionService.makeANewTransactionBetweenPersons(person4, 3, person7, "");
//
//        ListTransactionPagesDTO listTransactionPagesDTO = personService.displayTransactionsByPage(person1, 1, 3, "made");
//        System.out.println("page number " + listTransactionPagesDTO.getCurrentPage());
//        System.out.println("total number of pages : " + listTransactionPagesDTO.getTotalNumberOfPage());
//        System.out.println("total number of transactions : " + person1.getTransactionMadeList().size());
//        System.out.println(listTransactionPagesDTO.getTransactionsToDisplay().stream().map(t->t.getAmount() + " euros le "+t.getDate() + " à " + t.getTime()+"\n").collect(Collectors.joining()));
//
//        personService.deletePerson(person1);

//        List<PersonDTO> allPersons = personService.getAllActivePersonsDTO();
//        String result = allPersons.stream().map(person -> {
//            String identity = person.getFirstName() + " " + person.getLastName() + " " + person.getEmail() + "\n";
//            String money = "J'ai " + person.getAvailableBalance() + " euros\n";
//            String friends = "Mes amis sont :";
//            if (person.getGroup().isEmpty()) {
//                friends = friends + "je n'ai pas d'amis\n";
//            } else {
//                friends = friends + person.getGroup().stream().map(p -> p.getFirstName() + " " + p.getLastName() + "\n").collect(Collectors.joining());
//            }
//            String transactionsMade = "Les transactions que j'ai faites : ";
//            if (person.getTransactionMadeList().isEmpty()) {
//                transactionsMade = transactionsMade + "je n'ai pas fait de transaction\n";
//            } else {
//                transactionsMade = transactionsMade + person.getTransactionMadeList()
//                        .stream()
//                        .map(t -> {
//                            String sentenceResult;
//                            if (t.getDescription().contains("Transaction with bank account :")) {
//                                if (t.getAmount() > 0) {
//                                    sentenceResult = t.getAmount() + " euros du compte " + t.getReceiver() + " le " + t.getDate() + " à " + t.getTime() + "\n";
//                                } else {
//                                    sentenceResult = t.getAmount() + " euros pour le compte " + t.getReceiver() + " le " + t.getDate() + " à " + t.getTime() + "\n";
//                                }
//                            } else {
//                                sentenceResult = t.getAmount() + " euros pour " + t.getReceiver() + " le " + t.getDate() + " à " + t.getTime() + "\n";
//                            }
//                            return sentenceResult;
//                        })
//                        .collect(Collectors.joining());
//            }
//            String transactionsReceived = "Les transactions que j'ai reçues : ";
//            if (person.getTransactionReceivedList().isEmpty()) {
//                transactionsReceived = transactionsReceived + "je n'ai pas reçu de transaction\n";
//            } else {
//                transactionsReceived = transactionsReceived + person.getTransactionReceivedList().stream().map(t -> t.getAmount() + " euros de " + t.getSender() + " le " + t.getDate() + " à " + t.getTime() + "\n").collect(Collectors.joining());
//            }
//            String bankAccounts = "Mes comptes bancaires sont : ";
//            if (person.getBankAccountDTOList().isEmpty()) {
//                bankAccounts = bankAccounts + "je n'ai pas de compte bancaire\n";
//            } else {
//                bankAccounts = bankAccounts + person.getBankAccountDTOList().stream().map(account -> account.getUsualName() + " number : " + account.getIban() + "\n").collect(Collectors.joining());
//            }
//
//
//            return identity + money + friends + transactionsMade + transactionsReceived + bankAccounts + "\n";
//        }).collect(Collectors.joining());
//        System.out.println(result);
    }
}

