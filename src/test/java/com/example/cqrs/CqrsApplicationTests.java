package com.example.cqrs;

import com.example.cqrs.model.Address;
import com.example.cqrs.model.Contact;
import com.example.cqrs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class CqrsApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCRUD() {
        String userId = UUID.randomUUID().toString();
        log.debug("Testing {}",userId);

        userService.createUser(userId, "Cesare", "Mauri");

        Set<Contact> contacts = new HashSet<>();
        contacts.add(Contact.builder().type("Home").detail("Phone").build());
        contacts.add(Contact.builder().type("Work").detail("Mail").build());

        Set<Address> addresses = new HashSet<>();
        addresses.add(Address.builder().city("Lomagna").state("IT").postcode("23871").build());

        userService.updateUser(userId, contacts, addresses);

        var cont = userService.getContactByType(userId, "Fun");
        assertEquals(cont.size(), 0);

    }
}
