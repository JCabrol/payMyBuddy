package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.MessageToAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageToAdminRepository extends CrudRepository<MessageToAdmin,Integer> {
}
