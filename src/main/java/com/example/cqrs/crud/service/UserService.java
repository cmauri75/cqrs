package com.example.cqrs.crud.service;

import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import com.example.cqrs.crud.model.User;
import com.example.cqrs.crud.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Simple CRUD
 */
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public void createUser(String userId, String firstName, String lastName) {
        repository.addUser(userId, User.builder().userid(userId).firstName(firstName).lastName(lastName).build());
    }

    public void updateUser(String userId, Set<Contact> contacts, Set<Address> addresses) {
        User user = repository.getUser(userId);
        user.setContacts(contacts);
        user.setAddresses(addresses);
        repository.addUser(userId, user);
    }

    public Set<Contact> getContactByType(String userId, String contactType) {
        User user = repository.getUser(userId);
        Set<Contact> contacts = user.getContacts();
        return contacts.stream()
          .filter(c -> c.getType().equals(contactType))
          .collect(Collectors.toSet());
    }

    public Set<Address> getAddressByRegion(String userId, String state) {
        User user = repository.getUser(userId);
        Set<Address> addresses = user.getAddresses();
        return addresses.stream()
          .filter(a -> a.getState().equals(state))
          .collect(Collectors.toSet());
    }
}
