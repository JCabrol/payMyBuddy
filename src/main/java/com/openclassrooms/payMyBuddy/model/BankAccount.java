package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Bank_Account")
public class BankAccount {

    @Id
    @Column(name = "iban")
    private String iban;

    @Column(name = "bic")
    private String bic;

    @Column(name="usual_Name")
    private String usualName;

    @Column(name= "active_Bank_Account")
    private boolean activeBankAccount = true;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name="owner_Id", referencedColumnName="email")
    private Person owner;
}
