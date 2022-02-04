package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "transaction_Id")
    private int transactionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name="sender_Id", referencedColumnName="email")
    private Person sender;

    @Column(name = "date_Time")
    private LocalDateTime dateTime = LocalDateTime.now();

    @Column(name = "amount")
    private float amount;

    @Column(name = "description")
    private String description;


    public Transaction(Person sender, float amount){
        this.sender = sender;
        this.amount = amount;
    }

    public Transaction(Person sender, float amount,String description){
        this.sender = sender;
        this.amount = amount;
        this.description = description;
    }
}
