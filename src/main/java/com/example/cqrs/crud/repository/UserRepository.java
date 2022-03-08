package com.example.cqrs.crud.repository;

import com.example.cqrs.crud.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory Repo
 */
@Repository
public class UserRepository {
    private final Map<String, User> store = new HashMap<>();

    public void addUser(String userId, User u) {
        store.put(userId, u);
    }

    public User getUser(String userId) {
        return store.get(userId);
    }
}