package com.example.cqrs.cqrs.write.command;

import com.example.cqrs.crud.model.Address;
import com.example.cqrs.crud.model.Contact;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class UpdateUserCommand {
    private String userId;
    private Set<Address> addresses;
    private Set<Contact> contacts;
}