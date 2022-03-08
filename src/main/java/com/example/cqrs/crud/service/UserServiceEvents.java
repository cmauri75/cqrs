package com.example.cqrs.crud.service;

import com.example.cqrs.events.support.UserUtility;
import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import com.example.cqrs.crud.model.User;
import com.example.cqrs.events.events.*;
import com.example.cqrs.events.repository.EventStore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceEvents {
    private EventStore repository;

    public void createUser(String userId, String firstName, String lastName) {
        repository.addEvent(userId, UserCreatedEvent.builder().userId(userId).firstName(firstName).lastName(lastName).build());
    }

    public void updateUser(String userId, Set<Contact> contacts, Set<Address> addresses) {
        //recreates user from all events, cache or snapshots usage is a good idea
        User user = UserUtility.recreateUserState(repository, userId);

        //Check differences and add events for any found
        user.getContacts().stream()
                .filter(c -> !contacts.contains(c))
                .forEach(c -> repository.addEvent(
                        userId, UserContactRemovedEvent.builder().contactType(c.getType()).contactDetails(c.getDetail()).build()));
        contacts.stream()
                .filter(c -> !user.getContacts().contains(c))
                .forEach(c -> repository.addEvent(
                        userId, UserContactAddedEvent.builder().contactType(c.getType()).contactDetails(c.getDetail()).build()));
        user.getAddresses().stream()
                .filter(a -> !addresses.contains(a))
                .forEach(a -> repository.addEvent(
                        userId, UserAddressRemovedEvent.builder().city(a.getCity()).state(a.getState()).postCode(a.getPostcode()).build()));
        addresses.stream()
                .filter(a -> !user.getAddresses().contains(a))
                .forEach(a -> repository.addEvent(
                        userId, UserAddressAddedEvent.builder().city(a.getCity()).state(a.getState()).postCode(a.getPostcode()).build()));
    }

    public Set<Contact> getContactByType(String userId, String contactType) {
        User user = UserUtility.recreateUserState(repository, userId);
        return user.getContacts().stream()
                .filter(c -> c.getType().equals(contactType))
                .collect(Collectors.toSet());
    }

    public Set<Address> getAddressByRegion(String userId, String state) {
        User user = UserUtility.recreateUserState(repository, userId);
        return user.getAddresses().stream()
                .filter(a -> a.getState().equals(state))
                .collect(Collectors.toSet());
    }
}
