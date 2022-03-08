package com.example.cqrs.cqrs.read.model;

import com.example.cqrs.cqrs.write.model.Contact;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class UserContact {
    private Map<String, Set<Contact>> contactByType = new HashMap<>();
}