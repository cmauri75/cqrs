package com.example.cqrs.cqrs.read.projection;

import com.example.cqrs.cqrs.read.model.UserAddress;
import com.example.cqrs.cqrs.read.model.UserContact;
import com.example.cqrs.cqrs.read.query.AddressByRegionQuery;
import com.example.cqrs.cqrs.read.query.ContactByTypeQuery;
import com.example.cqrs.cqrs.read.repository.UserReadRepository;
import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserProjection {
    private UserReadRepository readRepository;

    public Set<Contact> handle(ContactByTypeQuery query) {
        UserContact userContact = readRepository.getUserContact(query.getUserId());
        var res = userContact.getContactByType()
                .get(query.getContactType());
        return res != null ? res : new HashSet<>();
    }

    public Set<Address> handle(AddressByRegionQuery query) {
        UserAddress userAddress = readRepository.getUserAddress(query.getUserId());
        var res = userAddress.getAddressByRegion()
                .get(query.getState());
        return res != null ? res : new HashSet<>();
    }
}