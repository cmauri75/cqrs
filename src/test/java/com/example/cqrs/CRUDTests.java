package com.example.cqrs;

import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import com.example.cqrs.crud.service.UserService;
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
class CRUDTests {

    @Autowired
    UserService userService;

    @Test
    public void testCRUD() {
        String userId = UUID.randomUUID().toString();
        log.debug("Testing {}", userId);

        //**** write part
        userService.createUser(userId, "Cesare", "Mauri");

        Set<Contact> contacts = new HashSet<>();
        contacts.add(Contact.builder().type("Home").detail("Phone").build());
        contacts.add(Contact.builder().type("Work").detail("Mail").build());

        Set<Address> addresses = new HashSet<>();
        addresses.add(Address.builder().city("Lomagna").state("IT").postcode("23871").build());

        userService.updateUser(userId, contacts, addresses);

        //*** Read part
        var cont = userService.getContactByType(userId, "Fun");
        assertEquals(0,cont.size());

        var cont2 = userService.getContactByType(userId, "Home");
        assertEquals(1,cont2.size());
        assertEquals(cont2.toArray()[0],Contact.builder().type("Home").detail("Phone").build());

        var cont3 = userService.getAddressByRegion(userId, "IT");
        assertEquals(1,cont3.size());
        assertEquals(cont3.toArray()[0],Address.builder().city("Lomagna").state("IT").postcode("23871").build());

    }
}
