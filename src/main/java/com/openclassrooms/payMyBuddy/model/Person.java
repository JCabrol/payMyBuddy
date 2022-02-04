package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "email")
    @NotBlank(message = "The email is mandatory.")
    @Email(message = "The email should be valid.")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "The password is mandatory.")
    private String password;

    @Column(name = "first_Name")
    private String firstName;

    @Column(name = "last_Name")
    private String lastName;

    @Column(name = "available_Balance")
    private float availableBalance;

    @Column(name = "active")
    private boolean active=true;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "relations",
            joinColumns = @JoinColumn(name = "relation_Instigator", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "relation_Receiver", referencedColumnName = "email"))
    private List<Person> relations = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<BankAccount> bankAccountList = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionMadeList = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionBetweenPersons> transactionReceivedList = new ArrayList<>();

//constructors needed (constructor with no argument, getters and setters are generated by lombok)

    public Person(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

//helper methods for BankAccounts

    public void addBankAccount(BankAccount bankAccount) {
        bankAccountList.add(bankAccount);
        bankAccount.setOwner(this);
    }

    public void removeBankAccount(BankAccount bankAccount) {
        bankAccountList.remove(bankAccount);
        bankAccount.setOwner(null);
    }

//helper methods for Transactions

    public void addTransactionMade(Transaction transaction) {
        transactionMadeList.add(transaction);
        transaction.setSender(this);
        if (transaction instanceof TransactionBetweenPersons) {
            ((TransactionBetweenPersons) transaction).getRecipient().getTransactionReceivedList().add((TransactionBetweenPersons) transaction);
        }
    }

    public void removeTransactionMade(Transaction transaction) {
        transactionMadeList.remove(transaction);
        transaction.setSender(null);
    }

    public void addTransactionReceived(TransactionBetweenPersons transaction) {
        transactionReceivedList.add(transaction);
        transaction.setRecipient(this);
    }

    public void removeTransactionReceived(TransactionBetweenPersons transaction) {
        transactionReceivedList.remove(transaction);
        transaction.setRecipient(null);
    }
    //helper methods for Group

    public void addPersonInGroup(Person person) {
        relations.add(person);
    }

    public void removePersonInGroup(Person person) {
        relations.remove(person);
    }

}