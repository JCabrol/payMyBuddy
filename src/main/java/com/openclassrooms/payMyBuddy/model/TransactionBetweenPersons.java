package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="Transaction_Between_Persons")
public class TransactionBetweenPersons extends Transaction{

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="receiver_Id", referencedColumnName="email")
    private Person recipient;

    // Constructors needed (constructor with no argument, getters and setters are generated by lombok)
    public TransactionBetweenPersons(Person sender, float amount, Person recipient,String description){
        super(sender, amount, description);
        this.recipient=recipient;
    }

}
