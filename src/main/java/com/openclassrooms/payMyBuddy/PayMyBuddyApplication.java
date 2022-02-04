package com.openclassrooms.payMyBuddy;

import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.Person;
import com.openclassrooms.payMyBuddy.model.TransactionBetweenPersons;
import com.openclassrooms.payMyBuddy.model.TransactionWithBank;
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
import java.util.Optional;
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


        PersonDTO person1 = personService.getPersonDTO("person1@mail.fr");
        PersonDTO person2 = personService.getPersonDTO("person2@mail.fr");
        PersonDTO person3 = personService.getPersonDTO("person3@mail.fr");
        PersonDTO person4 = personService.getPersonDTO("person4@mail.fr");
        PersonDTO person5 = personService.getPersonDTO("person5@mail.fr");
        PersonDTO person6 = personService.getPersonDTO("person6@mail.fr");
        PersonDTO person7 = personService.getPersonDTO("person7@mail.fr");
        PersonDTO person8 = personService.getPersonDTO("person8@mail.fr");

        personService.addMoney(person1, 50);
        personService.addMoney(person2, 400);
        personService.addMoney(person3, 1000);
        personService.addMoney(person4, 14);

//        personService.addPersonInGroup(person1, person3);
//        personService.addPersonInGroup(person1, person5);
//        personService.addPersonInGroup(person2, person6);
//        personService.addPersonInGroup(person3, person6);
//        personService.addPersonInGroup(person3, person7);
//        personService.addPersonInGroup(person3, person8);
//        personService.addPersonInGroup(person4, person7);



        transactionService.makeANewTransactionBetweenPersons(person1,14,person3, "dad's birthday");
        transactionService.makeANewTransactionBetweenPersons(person1,14,person5, "dad's birthday");
        transactionService.makeANewTransactionBetweenPersons(person2,100,person6, "");
        transactionService.makeANewTransactionBetweenPersons(person3,40,person6, "");
        transactionService.makeANewTransactionBetweenPersons(person3,400,person7, "");
        transactionService.makeANewTransactionBetweenPersons(person3,50,person8, "");
        transactionService.makeANewTransactionBetweenPersons(person4,3,person7, "");



       //personService.deletePerson(person1);

        List<PersonDTO> allPersons = personService.getAllPersonsDTO();
        String result = allPersons.stream().map(person->{
            String identity = person.getFirstName()+" "+person.getLastName()+" " +person.getEmail() + "\n";
            String money = "J'ai " + person.getAvailableBalance() + " euros\n";
            String friends = "Mes amis sont :";
            if (person.getGroup().isEmpty()){
                friends = friends + "je n'ai pas d'amis\n";
            }else{
                friends = friends+person.getGroup().stream().map(p->p.getFirstName()+" "+p.getLastName()+"\n").collect(Collectors.joining());
            }
            String transactionsMade = "Les transactions que j'ai faites : ";
            if(person.getTransactionMadeList().isEmpty()){
                transactionsMade = transactionsMade + "je n'ai pas fait de transaction\n";
            }else{
                transactionsMade = transactionsMade +person.getTransactionMadeList().stream().map(t->t.getAmount()+" euros pour "+t.getReceiver()+" le "+t.getDate()+ " à "+t.getTime()+"\n").collect(Collectors.joining());
            }
            String transactionsReceived = "Les transactions que j'ai reçues : ";
            if(person.getTransactionReceivedList().isEmpty()){
                transactionsReceived = transactionsReceived + "je n'ai pas reçu de transaction\n";
            }else{
                transactionsReceived = transactionsReceived +person.getTransactionReceivedList().stream().map(t->t.getAmount()+" euros de "+t.getSender()+" le "+t.getDate()+ " à "+t.getTime()+"\n").collect(Collectors.joining());
            }
            return identity + money +friends+transactionsMade+transactionsReceived+"\n";
        }).collect(Collectors.joining());
        System.out.println(result);
    }
}

