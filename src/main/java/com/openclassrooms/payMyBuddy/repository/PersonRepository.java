package com.openclassrooms.payMyBuddy.repository;

import com.openclassrooms.payMyBuddy.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person,String> {

    List<Person> findByActive(boolean active);


}
