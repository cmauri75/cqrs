package com.example.cqrs.cqrs.read.repository;

import com.example.cqrs.cqrs.read.model.UserAddress;
import com.example.cqrs.cqrs.read.model.UserContact;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory repository
 */
@Repository
public class UserReadRepository {
    private Map<String, UserAddress> userAddress = new HashMap<>();
    private Map<String, UserContact> userContact = new HashMap<>();

    public UserAddress getUserAddress(String userId) {
        return userAddress.get(userId);
    }

    public UserContact getUserContact(String userId) {
        return userContact.get(userId);
    }

    public void addUserAddress(String userId, UserAddress address) {
        userAddress.put(userId, address);
    }

    public void addUserContact(String userId, UserContact contact) {
        userContact.put(userId, contact);
    }
}