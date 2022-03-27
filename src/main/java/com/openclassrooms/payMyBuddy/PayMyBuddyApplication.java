package com.openclassrooms.payMyBuddy;

import com.openclassrooms.payMyBuddy.repository.PersonRepository;
import com.openclassrooms.payMyBuddy.repository.TransactionBetweenPersonsRepository;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PayMyBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
    }
}


