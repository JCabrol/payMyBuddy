package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "transactionId")
    private int transactionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "transactionMadeList")
    private Person sender;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Column(name = "amount")
    private float amount;

    @Column(name = "description")
    private String description;
}
