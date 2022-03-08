package com.example.cqrs.cqrs.write.repository;

import com.example.cqrs.cqrs.write.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory Repo
 */
@Repository
public class UserWriteRepository {
    private final Map<String, User> store = new HashMap<>();

    public void addUser(String userId, User u) {
        store.put(userId, u);
    }

    public User getUser(String userId) {
        return store.get(userId);
    }
}