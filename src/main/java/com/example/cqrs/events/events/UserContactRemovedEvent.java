package com.example.cqrs.events.events;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserContactRemovedEvent extends Event {
    private String contactType;
    private String contactDetails;
}