package com.example.cqrs.events.support;


import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import com.example.cqrs.crud.model.User;
import com.example.cqrs.events.events.*;
import com.example.cqrs.events.repository.EventStore;

import java.util.HashSet;
import java.util.UUID;

public class UserUtility {

    public static User recreateUserState(EventStore store, String userId) {
        User user = null;

        for (Event event : store.getEvents(userId)) {
            if (event instanceof UserCreatedEvent e) {
                user = User.builder().userid(UUID.randomUUID().toString()).firstName(e.getFirstName()).lastName(e.getLastName()).addresses(new HashSet<>()).contacts(new HashSet<>()).build();
            }
            if (user != null) {
                if (event instanceof UserAddressAddedEvent e) {
                    user.getAddresses().add(Address.builder().city(e.getCity()).state(e.getState()).postcode(e.getPostCode()).build());
                }
                if (event instanceof UserAddressRemovedEvent e) {
                    user.getAddresses().remove(Address.builder().city(e.getCity()).state(e.getState()).postcode(e.getPostCode()).build());
                }
                if (event instanceof UserContactAddedEvent e) {
                    user.getContacts().add(Contact.builder().type(e.getContactType()).detail(e.getContactDetails()).build());
                }
                if (event instanceof UserContactRemovedEvent e) {
                    user.getContacts().remove(Contact.builder().type(e.getContactType()).detail(e.getContactDetails()).build());
                }
            }
        }

        return user;
    }

}