package com.example.cqrs.cqrs.projector;

import com.example.cqrs.cqrs.read.model.UserAddress;
import com.example.cqrs.cqrs.read.model.UserContact;
import com.example.cqrs.cqrs.read.repository.UserReadRepository;
import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import com.example.cqrs.events.events.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserProjectorEvent {
    UserReadRepository readRepository;

    public void project(String userId, List<Event> events) {
        for (Event event : events) {
            if (event instanceof UserAddressAddedEvent e)
                apply(userId, e);
            if (event instanceof UserAddressRemovedEvent e)
                apply(userId, e);
            if (event instanceof UserContactAddedEvent e)
                apply(userId, e);
            if (event instanceof UserContactRemovedEvent e)
                apply(userId, e);
        }
    }

    public void apply(String userId, UserAddressAddedEvent event) {
        Address address = Address.builder().city(event.getCity()).state(event.getState()).postcode(event.getPostCode()).build();
        UserAddress userAddress = Optional.ofNullable(readRepository.getUserAddress(userId)).orElse(new UserAddress());
        Set<Address> addresses = Optional.ofNullable(userAddress.getAddressByRegion().get(address.getState())).orElse(new HashSet<>());
        addresses.add(address);
        userAddress.getAddressByRegion().put(address.getState(), addresses);
        readRepository.addUserAddress(userId, userAddress);
    }

    public void apply(String userId, UserAddressRemovedEvent event) {
        Address address = Address.builder().city(event.getCity()).state(event.getState()).postcode(event.getPostCode()).build();
        UserAddress userAddress = readRepository.getUserAddress(userId);
        if (userAddress != null) {
            Set<Address> addresses = userAddress.getAddressByRegion()
                    .get(address.getState());
            if (addresses != null)
                addresses.remove(address);
            readRepository.addUserAddress(userId, userAddress);
        }
    }

    public void apply(String userId, UserContactAddedEvent event) {
        Contact contact = Contact.builder().type(event.getContactType()).detail(event.getContactDetails()).build();
        UserContact userContact = Optional.ofNullable(readRepository.getUserContact(userId)).orElse(new UserContact());
        Set<Contact> contacts = Optional.ofNullable(userContact.getContactByType().get(contact.getType())).orElse(new HashSet<>());
        contacts.add(contact);
        userContact.getContactByType().put(contact.getType(), contacts);
        readRepository.addUserContact(userId, userContact);
    }

    public void apply(String userId, UserContactRemovedEvent event) {
        Contact contact = Contact.builder().type(event.getContactType()).detail(event.getContactDetails()).build();
        UserContact userContact = readRepository.getUserContact(userId);
        if (userContact != null) {
            Set<Contact> contacts = userContact.getContactByType().get(contact.getType());
            if (contacts != null)
                contacts.remove(contact);
            readRepository.addUserContact(userId, userContact);
        }
    }

}
