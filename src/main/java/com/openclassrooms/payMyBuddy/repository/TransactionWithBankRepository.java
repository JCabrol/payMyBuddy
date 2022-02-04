package com.openclassrooms.payMyBuddy.repository;

import com.openclassrooms.payMyBuddy.model.TransactionWithBank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionWithBankRepository extends CrudRepository<TransactionWithBank,Integer> {
}
