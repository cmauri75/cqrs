package com.example.cqrs.cqrs.projector;

import com.example.cqrs.cqrs.read.model.UserAddress;
import com.example.cqrs.cqrs.read.model.UserContact;
import com.example.cqrs.cqrs.read.repository.UserReadRepository;
import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import com.example.cqrs.crud.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class UserProjector {
    private UserReadRepository readRepository;

    public void project(User user) {
        UserContact userContact = Optional.ofNullable(readRepository.getUserContact(user.getUserid())).orElse(new UserContact());

        Map<String, Set<Contact>> contactByType = new HashMap<>();
        if (user.getContacts() != null) {
            for (Contact contact : user.getContacts()) {
                Set<Contact> contacts = Optional.ofNullable(contactByType.get(contact.getType())).orElse(new HashSet<>());
                contacts.add(contact);
                contactByType.put(contact.getType(), contacts);
            }
        }
        userContact.setContactByType(contactByType);
        readRepository.addUserContact(user.getUserid(), userContact);

        UserAddress userAddress = Optional.ofNullable(readRepository.getUserAddress(user.getUserid())).orElse(new UserAddress());
        Map<String, Set<Address>> addressByRegion = new HashMap<>();
        if (user.getAddresses() != null) {
            for (Address address : user.getAddresses()) {
                Set<Address> addresses = Optional.ofNullable(addressByRegion.get(address.getState())).orElse(new HashSet<>());
                addresses.add(address);
                addressByRegion.put(address.getState(), addresses);
            }
        }
        userAddress.setAddressByRegion(addressByRegion);
        readRepository.addUserAddress(user.getUserid(), userAddress);
    }
}
