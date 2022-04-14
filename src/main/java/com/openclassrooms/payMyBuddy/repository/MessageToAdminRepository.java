package com.openclassrooms.payMyBuddy.repository;

import com.openclassrooms.payMyBuddy.model.MessageToAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageToAdminRepository extends CrudRepository<MessageToAdmin,Integer> {
}
