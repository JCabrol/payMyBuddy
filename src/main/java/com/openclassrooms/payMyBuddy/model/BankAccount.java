package com.openclassrooms.payMyBuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "BankAccount")
public class BankAccount {

    @Id
    @Column(name = "iban")
    private String iban;

    @Column(name = "bic")
    private String bic;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn (name = "bankAccountList")
    private Person owner;
}
