package com.openclassrooms.payMyBuddy.repository;

import com.openclassrooms.payMyBuddy.model.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount,String> {
}
