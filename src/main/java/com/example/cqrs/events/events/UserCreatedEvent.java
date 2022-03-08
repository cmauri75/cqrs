package com.example.cqrs.events.events;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserCreatedEvent extends Event {
    private String userId;
    private String firstName;
    private String lastName;
}
