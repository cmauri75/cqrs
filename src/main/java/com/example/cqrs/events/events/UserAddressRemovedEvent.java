package com.example.cqrs.events.events;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserAddressRemovedEvent extends Event {
    private String city;
    private String state;
    private String postCode;
}