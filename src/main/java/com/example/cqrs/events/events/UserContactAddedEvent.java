package com.example.cqrs.events.events;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserContactAddedEvent extends Event {
    private String contactType;
    private String contactDetails;
}