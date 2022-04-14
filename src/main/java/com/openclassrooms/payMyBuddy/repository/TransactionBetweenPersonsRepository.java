package com.openclassrooms.payMyBuddy.repository;

import com.openclassrooms.payMyBuddy.model.TransactionBetweenPersons;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionBetweenPersonsRepository extends CrudRepository<TransactionBetweenPersons, Integer> {
}
