package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, String> {

    List<Person> findByActive(boolean active);


}
