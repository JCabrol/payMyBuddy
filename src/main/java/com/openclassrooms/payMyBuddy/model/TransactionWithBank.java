package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TransactionWithBank")
public class TransactionWithBank extends Transaction{


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "iban")
    private BankAccount recipient;

}
