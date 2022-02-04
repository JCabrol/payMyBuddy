package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Transaction_With_Bank")
public class TransactionWithBank extends Transaction{


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="receiver_Id", referencedColumnName="iban")
    private BankAccount recipient;

    public TransactionWithBank(Person sender,float amount,BankAccount recipient){
        super(sender,amount);
        this.recipient = recipient;
    }

    public TransactionWithBank(Person sender,float amount,BankAccount recipient,String description){
        super(sender,amount,description);
        this.recipient = recipient;
    }

}
