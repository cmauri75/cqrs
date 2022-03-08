package com.example.cqrs;

import com.example.cqrs.cqrs.projector.UserProjector;
import com.example.cqrs.cqrs.read.projection.UserProjection;
import com.example.cqrs.cqrs.read.query.AddressByRegionQuery;
import com.example.cqrs.cqrs.read.query.ContactByTypeQuery;
import com.example.cqrs.cqrs.write.aggregate.UserAggregate;
import com.example.cqrs.cqrs.write.command.CreateUserCommand;
import com.example.cqrs.cqrs.write.command.UpdateUserCommand;
import com.example.cqrs.cqrs.write.model.User;
import com.example.cqrs.cqrs.write.model.Address;
import com.example.cqrs.cqrs.write.model.Contact;
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
class CQRSTests {

    @Autowired
    UserAggregate userAggregate;

    @Autowired
    UserProjection userProjection;

    @Autowired
    UserProjector userProjector;

    @Test
    public void testCQRS() {
        String userId = UUID.randomUUID().toString();
        log.debug("Testing {}", userId);

        //**** Write part
        //Create user and align read
        CreateUserCommand createUserCommand = CreateUserCommand.builder().userId(userId).firstName("Cesare").lastName("Mauri").build();
        User user = userAggregate.handleCreateUserCommand(createUserCommand);
        userProjector.project(user);

        Set<Contact> contacts = new HashSet<>();
        contacts.add(Contact.builder().type("Home").detail("Phone").build());
        contacts.add(Contact.builder().type("Work").detail("Mail").build());

        Set<Address> addresses = new HashSet<>();
        addresses.add(Address.builder().city("Lomagna").state("IT").postcode("23871").build());

        //Add contacts and addresses and align read
        UpdateUserCommand updateUserCommand = UpdateUserCommand.builder().userId(userId).addresses(addresses).contacts(contacts).build();
        user = userAggregate.handleUpdateUserCommand(updateUserCommand);
        userProjector.project(user);


        //****** Read Part
        ContactByTypeQuery contactByTypeQuery = ContactByTypeQuery.builder().userId(userId).contactType("Fun").build();
        var cont = userProjection.handle(contactByTypeQuery);
        assertEquals(0,cont.size());

        ContactByTypeQuery contactByTypeQuery2 = ContactByTypeQuery.builder().userId(userId).contactType("Home").build();
        var cont2 = userProjection.handle(contactByTypeQuery2);
        assertEquals(1,cont2.size());
        assertEquals(cont2.toArray()[0], Contact.builder().type("Home").detail("Phone").build());

        AddressByRegionQuery contactByTypeQuery3 = AddressByRegionQuery.builder().userId(userId).state("IT").build();
        var cont3 = userProjection.handle(contactByTypeQuery3);
        assertEquals(1,cont3.size());
        assertEquals(cont3.toArray()[0], Address.builder().city("Lomagna").state("IT").postcode("23871").build());

    }
}
