package com.openclassrooms.payMyBuddy.unitTests;

import com.openclassrooms.payMyBuddy.repository.MessageToAdminRepository;
import com.openclassrooms.payMyBuddy.repository.TransactionBetweenPersonsRepository;
import com.openclassrooms.payMyBuddy.service.MessageToAdminService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@Tag("ServiceTests")
@Slf4j
@SpringBootTest
public class MessageToAdminServiceTest {

    @Autowired
    private MessageToAdminService messageToAdminService;

    @MockBean
    private MessageToAdminRepository messageToAdminRepository;


}
