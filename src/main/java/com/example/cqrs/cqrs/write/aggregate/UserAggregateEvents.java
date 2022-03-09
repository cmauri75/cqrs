package com.example.cqrs.cqrs.write.aggregate;

import com.example.cqrs.cqrs.write.command.CreateUserCommand;
import com.example.cqrs.cqrs.write.command.UpdateUserCommand;
import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import com.example.cqrs.crud.model.User;
import com.example.cqrs.events.events.*;
import com.example.cqrs.events.repository.EventStore;
import com.example.cqrs.events.support.UserUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserAggregateEvents {
    private EventStore writeRepository;

    public List<Event> handleCreateUserCommand(CreateUserCommand command) {
        Event event = UserCreatedEvent.builder().userId(command.getUserId()).firstName(command.getFirstName()).lastName(command.getLastName()).build();
        writeRepository.addEvent(command.getUserId(), event);
        return Arrays.asList(event);
    }

    public List<Event> handleUpdateUserCommand(UpdateUserCommand command) {
        User user = UserUtility.recreateUserState(writeRepository, command.getUserId());
        List<Event> events = new ArrayList<>();

        List<Contact> contactsToRemove = user.getContacts().stream()
                .filter(c -> !command.getContacts().contains(c))
                .collect(Collectors.toList());
        for (Contact contact : contactsToRemove) {
            UserContactRemovedEvent contactRemovedEvent = UserContactRemovedEvent.builder().contactType(contact.getType()).contactDetails(contact.getDetail()).build();
            events.add(contactRemovedEvent);
            writeRepository.addEvent(command.getUserId(), contactRemovedEvent);
        }
        List<Contact> contactsToAdd = command.getContacts().stream()
                .filter(c -> !user.getContacts().contains(c))
                .collect(Collectors.toList());
        for (Contact contact : contactsToAdd) {
            UserContactAddedEvent contactAddedEvent = UserContactAddedEvent.builder().contactType(contact.getType()).contactDetails(contact.getDetail()).build();
            events.add(contactAddedEvent);
            writeRepository.addEvent(command.getUserId(), contactAddedEvent);
        }

        List<Address> addressesToRemove = user.getAddresses()
                .stream()
                .filter(a -> !command.getAddresses()
                        .contains(a))
                .collect(Collectors.toList());
        for (Address address : addressesToRemove) {
            UserAddressRemovedEvent addressRemovedEvent = UserAddressRemovedEvent.builder().city(address.getCity()).state(address.getState()).postCode(address.getPostcode()).build();
            events.add(addressRemovedEvent);
            writeRepository.addEvent(command.getUserId(), addressRemovedEvent);
        }

        List<Address> addressesToAdd = command.getAddresses()
                .stream()
                .filter(a -> !user.getAddresses()
                        .contains(a))
                .collect(Collectors.toList());
        for (Address address : addressesToAdd) {
            UserAddressAddedEvent addressAddedEvent = UserAddressAddedEvent.builder().city(address.getCity()).state(address.getState()).postCode(address.getPostcode()).build();
            events.add(addressAddedEvent);
            writeRepository.addEvent(command.getUserId(), addressAddedEvent);
        }

        return events;
    }
}
