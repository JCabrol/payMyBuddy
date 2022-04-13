package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.TransactionBetweenPersons;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionBetweenPersonsRepository extends CrudRepository<TransactionBetweenPersons, Integer> {
}
